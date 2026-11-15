package com.projectrevivesolutions.layertime.controller.signatures

import android.content.Context
import com.projectrevivesolutions.layertime.controller.ai.*
import com.projectrevivesolutions.layertime.controller.ai.runtime.BundledModelFactory
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import com.projectrevivesolutions.layertime.controller.state.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.security.MessageDigest

/** Own inference worker; collecting events never waits for its model or disk writes. */
class LiveSignatureAnalysis(private val context:Context) {
    data class State(val status:String="Waiting for relevant evidence",val finding:String?=null,val key:String?=null)
    companion object {
        private val mutable=MutableStateFlow(State())
        val state=mutable.asStateFlow()
    }
    private val attempted=linkedSetOf<String>()
    @OptIn(FlowPreview::class)
    suspend fun run()=withContext(Dispatchers.Default) {
        val coordinator=AiCoordinator(BundledModelFactory.production(context))
        ControllerRepository.signatureRecords.debounce(750).collect {rows ->
            val input=LiveSignatureInput.prepare(rows)
            if(input==null) {
                if(mutable.value.key!=null)mutable.value=mutable.value.copy(status="Previous recorded assessment; waiting for relevant evidence")
                return@collect
            }
            if(!attempted.add(input.key))return@collect
            if(attempted.size>256)attempted.remove(attempted.first())
            mutable.value=mutable.value.copy(status=if(mutable.value.finding==null)"Analyzing preserved evidence" else "Analyzing newer evidence; previous assessment retained")
            val digest=MessageDigest.getInstance("SHA-256").digest(input.key.toByteArray()).joinToString(""){"%02x".format(it)}
            try {
                // Persist the immutable input first, on this worker, never on BLE callbacks.
                val dir=File(context.filesDir,"signature-assessments").apply {check(mkdirs() || isDirectory)}
                val file=File(dir,"$digest.json")
                fun save(value:Map<String,Any?>) {
                    val atomic=android.util.AtomicFile(file)
                    val stream=atomic.startWrite()
                    try {stream.write(FlowJson.write(value).toByteArray());atomic.finishWrite(stream)}
                    catch(ex:Exception){atomic.failWrite(stream);throw ex}
                }
                val existing=runCatching {FlowJson.parse(file.readText()) as Map<*,*>}.getOrNull()
                if(existing?.get("status")=="AI analysis complete" && existing["input"]==FlowJson.parse(input.json)) {
                    mutable.value=State("Recorded AI assessment restored",existing["finding"] as? String,input.key)
                    return@collect
                }
                val identity=coordinator.availability().manifest
                val inputRecord=mapOf("schema" to "vixen-live-signature/1","input" to FlowJson.parse(input.json),
                    "source" to input.records.map {mapOf("producer" to it.producerAddress,"sessionId" to it.sessionId,
                        "eventId" to it.eventId,"detail" to it.detail,"firstUptimeMs" to it.sourceFirstUptimeMs)},
                    "timeBasis" to "same-producer-session-uptime","modelSha256" to identity?.sha256,"modelVersion" to identity?.version,"status" to "pending")
                save(inputRecord)
                val system=context.assets.open("ai/production/signature-system.txt").bufferedReader().use {it.readText()}
                val schema=context.assets.open("ai/production/signature-schema.json").bufferedReader().use {it.readText()}
                val request=LocalModelRequest(AiTask.INTERPRET_SIGNATURE,listOf(PromptMessage(PromptMessage.Role.SYSTEM,system),
                    PromptMessage(PromptMessage.Role.USER,input.json)),schema,64)
                var rawOutput:String?=null
                val result=coordinator.execute(request,120000) {raw ->
                    rawOutput=raw
                    val parsed=FlowJson.parse(raw) as? Map<*,*> ?: error("Object required")
                    require(parsed.keys==setOf("pattern"))
                    val pattern=parsed["pattern"] as? String ?: error("Pattern required")
                    require(pattern in setOf("disconnect_sequence_then_remote_id","disconnect_sequence_only","remote_id_only",
                        "reverse_order","outside_window","insufficient_timing","no_relevant_pattern"))
                    pattern
                }
                val current=LiveSignatureInput.prepare(ControllerRepository.signatureRecords.value)?.key==input.key
                val pattern=(result as? AiOutcome.Completed)?.value
                // This check rejects unsupported model proposals. Rejection is a failure, never a repaired pass.
                val relations=(FlowJson.parse(input.json) as Map<*,*>)["relations"] as Map<*,*>
                val expected=when {
                    relations["clockAligned"]!=true || relations["disconnectBeforeExchange"]==null || relations["remoteIdAfterExchange"]==null -> "insufficient_timing"
                    relations["disconnectBeforeExchange"]!=true -> "remote_id_only"
                    relations["remoteIdAfterExchange"]!=true -> "reverse_order"
                    relations["withinWindow"]!=true -> "outside_window"
                    else -> "disconnect_sequence_then_remote_id"
                }
                val supported=pattern==null || pattern==expected
                val finding=if(pattern!=null && supported) when(pattern) {
                    "disconnect_sequence_then_remote_id" -> "Deauthentication and EAPOL observations were followed by an unverified Remote ID broadcast after ${input.gapMs?.div(1000)} seconds. This is a candidate sequence for review; it does not prove an outage, a coordinated attack, or hostile drone intent."
                    "reverse_order" -> "Remote ID preceded the disconnect sequence; the expected follow-on order is not supported."
                    "outside_window" -> "The observations fall outside the configured 90-second interval."
                    "insufficient_timing" -> "The evidence does not establish the required event timing."
                    else -> "The recorded observations do not establish the complete expected sequence. Missing evidence is not an all-clear."
                } else null
                val status=when { !current->"Historical assessment; source or evidence changed"; !supported->"AI result rejected; original evidence retained";
                    result is AiOutcome.Completed->"AI analysis complete";result is AiOutcome.Unavailable->"AI model unavailable";
                    result is AiOutcome.TimedOut->"AI analysis timed out; collection continues";else->"AI analysis incomplete; evidence retained"}
                save(inputRecord+mapOf("status" to status,"modelOutcome" to result.toString(),"rawOutput" to rawOutput,"finding" to finding,
                    "completedEpochMs" to System.currentTimeMillis(),"currentAtCompletion" to current))
                mutable.value=State(status,finding,input.key)
                val delivered=if(current && supported && pattern=="disconnect_sequence_then_remote_id" && finding!=null) {
                    val quiet=(ControllerRepository.status.value?.flags ?: 0) and com.projectrevivesolutions.layertime.controller.link.LinkConstants.StatusFlag.SLEEP_MODE != 0
                    SignatureNotifications(context).post(digest,finding,quiet)
                } else false
                FieldObservationJournal.record("signature_analysis",mapOf("key" to input.key,"status" to status,"modelUsed" to (result is AiOutcome.Completed),"pattern" to pattern,"supported" to supported,"current" to current,"notificationPosted" to delivered))
            } catch(ex:CancellationException){throw ex}
            catch(ex:Exception){mutable.value=mutable.value.copy(status="AI analysis unavailable; previous assessment and evidence retained")}
        }
    }
}
