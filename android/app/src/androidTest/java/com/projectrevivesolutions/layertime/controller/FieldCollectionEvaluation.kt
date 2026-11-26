package com.projectrevivesolutions.layertime.controller

import android.content.Context
import android.os.Bundle
import com.projectrevivesolutions.layertime.controller.ai.*
import com.projectrevivesolutions.layertime.controller.ai.runtime.*
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest

/** Private, explicitly unqualified on-device field evaluation. Never changes source evidence. */
internal fun runFieldCollectionEvaluation(context:Context,result:Bundle,label:String)=runBlocking {
    check(BuildConfig.DEBUG && label.matches(Regex("[a-z0-9-]{1,60}")))
    val inputFile=File(context.cacheDir,"field-collection-evaluation.json")
    val bytes=inputFile.readBytes();check(bytes.size<=500000)
    val input=FlowJson.parse(bytes.toString(Charsets.UTF_8)) as Map<*,*>
    val cases=input["cases"] as List<*>;check(cases.size in 1..16)
    val provider=BundledModelFactory.development(context)
    val manifest=requireNotNull(provider.availability().manifest)
    check(!manifest.blueTeamTrained && AiTask.EXPLAIN_EVIDENCE in manifest.tasks)
    val file=File(context.filesDir,"diagnostics/$label.jsonl");check(!file.exists()) {"Preserve previous results"}
    file.parentFile?.mkdirs()
    fun digest(data:ByteArray)=MessageDigest.getInstance("SHA-256").digest(data).joinToString(""){"%02x".format(it)}
    try {
        for(value in cases) {
            val item=value as Map<*,*>;val facts=(item["facts"] as Map<*,*>).entries.associate {it.key as String to it.value}
            val snapshot=FieldShadowTasks.Snapshot(item["runId"] as String,item["summarySequence"] as Long,
                item["hunterAddress"] as String,(item["sessionId"] as Long).toInt(),item["eventId"] as Long,
                item["summaryReceivedEpochMs"] as Long,facts)
            val original=FieldShadowTasks.request(snapshot)
            val baselineError=runCatching {LocalPromptFormat.render(original,LocalPromptFormat.COMPACT_2507)}.exceptionOrNull()?.message
            // Isolate the missing-schema correction; retain baseline and actual request.
            var request=original.copy(messages=original.messages.mapIndexed {i,m->
                if(i==0 && !m.content.contains(original.outputSchema))m.copy(content=m.content+"\nOUTPUT SCHEMA\n"+original.outputSchema) else m})
            var packetFacts:FieldPacketBrief.Facts?=null
            if(item["kind"]=="decoded_capture") {
                fun number(key:String)=(facts[key] as Long).toInt()
                packetFacts=FieldPacketBrief.Facts(facts["sourcePcapSha256"] as String,
                    number("savedFrames"),number("observedWindowFrames"),number("droppedWindowFrames"),
                    number("deauthenticationFrames"),number("eapolFrames"),number("malformedFrames"))
                request=FieldPacketBrief.request(packetFacts)
            }
            val row=linkedMapOf<String,Any?>("caseId" to item["id"],"inputFileSha256" to digest(bytes),
                "snapshotSha256" to snapshot.sha256,"modelId" to manifest.id,"modelVersion" to manifest.version,
                "weightsSha256" to manifest.sha256,"releaseEligible" to false,"trainingApproved" to false,
                "semanticReviewRequired" to true,"baselinePromptError" to baselineError,
                "facts" to facts,"messages" to request.messages.map {mapOf("role" to it.role.name,"content" to it.content)},
                "outputSchema" to request.outputSchema,"maxOutputTokens" to request.maxOutputTokens)
            val start=android.os.SystemClock.elapsedRealtime()
            try {
                check(AiCoordinator(provider).execute(request){it} is AiOutcome.Unavailable)
                val tokens=withTimeout(120000){provider.countInputTokens(request)}
                row["inputTokens"]=tokens;check(tokens+request.maxOutputTokens<=manifest.contextTokens)
                val generated=android.os.SystemClock.elapsedRealtime()
                val response=withTimeout(90000){provider.generate(request)}
                row["generationMs"]=android.os.SystemClock.elapsedRealtime()-generated
                row["output"]=response.json
                row["identityValid"]=response.modelId==manifest.id && response.modelVersion==manifest.version && response.toolCalls.isEmpty()
                val parsed=runCatching {FieldShadowTasks.decode(response.json)}.getOrNull()
                row["formatValid"]=parsed!=null
                if(parsed!=null)row["requiredReportFactProblems"]=packetFacts?.let {FieldPacketBrief.reportProblems(it,parsed)}
                    ?: FieldShadowTasks.reportProblems(snapshot,parsed)
            } catch(ex:TimeoutCancellationException){row["error"]="timeout"}
            catch(ex:CancellationException){throw ex}
            catch(ex:Exception){row["error"]=ex.javaClass.simpleName+": "+ex.message}
            row["totalMs"]=android.os.SystemClock.elapsedRealtime()-start
            file.appendText(FlowJson.write(row)+"\n")
            android.util.Log.i("VixenFieldEvaluation","Saved "+item["id"]+" "+row["totalMs"]+"ms")
        }
        result.putString("field_ai_evaluation","Generated ${cases.size} cases; semantic review required")
        result.putString("field_ai_report",file.absolutePath)
    } finally {(provider as? AutoCloseable)?.close()}
}
