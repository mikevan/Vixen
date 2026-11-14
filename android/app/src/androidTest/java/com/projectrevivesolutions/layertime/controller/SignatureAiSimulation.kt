package com.projectrevivesolutions.layertime.controller
import android.content.Context
import android.os.Bundle
import com.projectrevivesolutions.layertime.controller.signatures.*
import com.projectrevivesolutions.layertime.controller.ai.*
import com.projectrevivesolutions.layertime.controller.ai.runtime.BundledModelFactory
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest

/** Quarantine is outside model input. Real offline inference, no radio or field writes. */
internal fun runSignatureAiSimulation(context:Context,fixtures:Context,result:Bundle,suiteName:String="signature-grounding-v1",positiveProofOnly:Boolean=false)=runBlocking {
    check(BuildConfig.DEBUG)
    check(suiteName in setOf("signature-grounding-v1","evacuation-policy-v1"))
    val suiteBytes=fixtures.assets.open("model-evaluations/$suiteName.json").use {it.readBytes()}
    val suite=FlowJson.parse(suiteBytes.toString(Charsets.UTF_8)) as Map<*,*>
    check(suite["quarantined"]==true && suite["trainingApproved"]==false && suite["schemaVersion"]==1L)
    val dir=File(context.filesDir,"diagnostics/quarantined-model-evaluation-${java.util.UUID.randomUUID()}").apply {check(mkdirs())}
    File(dir,"suite.json").writeBytes(suiteBytes)
    result.putString("simulation_artifact",dir.name)
    val defaultSignature=SignatureCatalog.decode(context.assets.open("signatures/library-v1.json").bufferedReader().use {it.readText()}).single().signature
    val provider=BundledModelFactory.development(context)
    val manifest=requireNotNull(provider.availability().manifest);check(!manifest.blueTeamTrained)
    fun digest(bytes:ByteArray)=MessageDigest.getInstance("SHA-256").digest(bytes).joinToString(""){"%02x".format(it)}
    try {
        val allCases=suite["cases"] as List<*>
        val selected=if(positiveProofOnly) {
            check(suiteName=="evacuation-policy-v1")
            val positive=allCases.single {(it as Map<*,*>)["id"]=="evacuation-positive"}
            listOf(positive,positive)
        } else allCases
        for((attempt,value) in selected.withIndex()) {
            val case=value as Map<*,*>;val name=(case["id"] as String)+if(positiveProofOnly) "-attempt-${attempt+1}" else ""
            val packet=FieldPacketBrief.Facts("a".repeat(64),64,103,39,18,0,0)
            val request=if(case["kind"]=="packet")FieldPacketBrief.request(packet) else {
                val evacuation=case["kind"]=="evacuation"
                val signature=if(evacuation)AttackSignatures.Signature("evacuation-event",1,"wifi-ble-outage","drone-observation",90000) else defaultSignature
                val firstAt=case["firstAtMs"] as? Long ?: 1000L
                val run=java.util.UUID.randomUUID().toString()
                val limits=listOf("Only the supplied slice intervals are covered; continuous coverage is unavailable.")
                val first=AttackSignatures.Slice("slice-1",run,"observer-clock-1",maxOf(0,firstAt-1000),maxOf(10000,firstAt+1000),
                    listOf(AttackSignatures.Observation("record-1",signature.firstKind,firstAt,"Hunter","slice-1#record-1")),limits)
                val slices=mutableListOf(first)
                val follow=case["followAtMs"] as? Long
                if(follow!=null)slices.add(AttackSignatures.Slice("slice-2",run,"observer-clock-1",maxOf(0,follow-1000),follow+1000,
                    listOf(AttackSignatures.Observation("record-2","drone-observation",follow,"independent observer","slice-2#record-2")),limits))
                val unmapped=case["unmappedFollowAtMs"] as? Long
                if(unmapped!=null)slices.add(AttackSignatures.Slice("slice-3",run,"observer-clock-2-unmapped",60000,70000,
                    listOf(AttackSignatures.Observation("record-3","drone-observation",unmapped,"second observer","slice-3#record-3")),limits))
                val evidenceSlices=if(evacuation && positiveProofOnly) {
                    val saved=mutableListOf<Map<String,Any?>>()
                    val preserver=LiveEvidenceSlices({snapshot->
                        saved.add(snapshot)
                        File(dir,"$name-slices.jsonl").appendText(FlowJson.write(mapOf("quarantined" to true,"fields" to snapshot))+"\n")
                    })
                    val observed=slices.flatMap {it.observations}.sortedBy {it.atMs}
                    var sequence=0L
                    fun receive(at:Long) {
                        val o=observed.firstOrNull {it.atMs==at}
                        val fields=linkedMapOf<String,Any?>("hunterAddress" to "02:00:00:00:00:01","sessionId" to 1)
                        if(o!=null)fields.putAll(mapOf("eventId" to (observed.indexOf(o)+1).toLong(),"count" to 1,
                            "confidence" to 2,"ageSeconds" to 0,"observationKind" to o.kind,"producer" to o.producer))
                        preserver.accept(LiveEvidenceSlices.Record(run,++sequence,at,if(o==null)"status" else "event_summary",fields,true))
                    }
                    val beginning=maxOf(0,observed.first().atMs-10000)
                    val ending=observed.last().atMs+10000
                    for(at in beginning..ending step 1000)receive(at)
                    receive(ending+1)
                    val closed=saved.filter {it["schema"]=="vixen-signature-slice/1" && it["state"]=="CLOSED"}
                    check(closed.size==2 && closed.all {it["recordsTruncated"]==false})
                    closed.map {snapshot->
                        val id=snapshot["sliceId"] as String
                        val observations=(snapshot["records"] as List<*>).mapNotNull {entry->
                            val row=entry as Map<*,*>;val fields=row["fields"] as Map<*,*>
                            val kind=fields["observationKind"] as? String
                            if(kind==null)null else AttackSignatures.Observation("$run:${row["sequence"]}",kind,
                                row["receivedMonotonicMs"] as Long,fields["producer"] as String,"$id#${row["sequence"]}")
                        }
                        AttackSignatures.Slice(id,run,"observer-clock-1",snapshot["requestedStartMs"] as Long,
                            snapshot["requestedEndMs"] as Long,observations,limits+
                                "Observation timestamps share the supplied common clock; receipt timing does not establish RF timing.")
                    }
                } else slices
                val a=AttackSignatures.assess(signature,evidenceSlices).single()
                check(a.state.name==case["expectedState"])
                val original=SignatureBrief.request(a)
                if(evacuation) {
                    EvacuationPolicy.request(a,EvacuationPolicy.Outage(120,0,80,0,true,true,0))
                } else if(unmapped==null)original else {
                    val facts=FlowJson.parse(original.messages[1].content.substringAfter("\n")) as Map<*,*>
                    val contextFacts=(facts["capture_context"] as Map<*,*>).entries.associate {it.key as String to it.value}+
                        mapOf("otherObservations" to listOf(
                            mapOf("kind" to "drone-observation","observedAtMs" to follow,"clockDomain" to "observer-clock-1","relation" to "earlier than flood"),
                            mapOf("kind" to "drone-observation","observedAtMs" to unmapped,"clockDomain" to "observer-clock-2-unmapped","relation" to "no clock mapping supplied")),
                            "capturedNetworkName" to case["capturedNetworkName"])
                    val enriched=facts.entries.associate {it.key as String to it.value}+mapOf("capture_context" to contextFacts)
                    original.copy(messages=listOf(original.messages[0],original.messages[1].copy(content="PRESERVED SIGNATURE EVIDENCE\n"+FlowJson.write(enriched))))
                }
            }
            // This checks model-visible content only, never the harness metadata.
            check(request.messages.none {it.content.contains("SIMULATION",true) || it.content.contains("quarantined",true) || it.content.contains("fictional",true)})
            check(AiCoordinator(provider).execute(request){it} is AiOutcome.Unavailable)
            val input=FlowJson.write(mapOf("case" to name,"quarantined" to true,"trainingApproved" to false,
                "appVersionCode" to BuildConfig.VERSION_CODE,"modelId" to manifest.id,"modelVersion" to manifest.version,"weightsSha256" to manifest.sha256,
                "releaseEligible" to false,"suiteSha256" to digest(suiteBytes),
                "messages" to request.messages.map {mapOf("role" to it.role.name,"content" to it.content)},
                "outputSchema" to request.outputSchema,"maxOutputTokens" to request.maxOutputTokens))
            File(dir,"$name-input.json").writeText(input)
            val row=linkedMapOf<String,Any?>("case" to name,"quarantined" to true,"releaseEligible" to false,"inputSha256" to digest(input.toByteArray()))
            val start=android.os.SystemClock.elapsedRealtime()
            try {
                val tokens=withTimeout(180000){provider.countInputTokens(request)}
                row["inputTokens"]=tokens;check(tokens+request.maxOutputTokens<=manifest.contextTokens)
                val generated=android.os.SystemClock.elapsedRealtime()
                val response=withTimeout(240000){provider.generate(request)}
                row["generationMs"]=android.os.SystemClock.elapsedRealtime()-generated;row["output"]=response.json
                row["identityValid"]=response.modelId==manifest.id && response.modelVersion==manifest.version && response.toolCalls.isEmpty()
                val parsed=runCatching {FieldShadowTasks.decode(response.json)}.getOrNull()
                row["formatValid"]=parsed!=null
                if(case["kind"]=="packet" && parsed!=null)row["packetFactProblems"]=FieldPacketBrief.reportProblems(packet,parsed)
                row["semanticReviewRequired"]=true
            } catch(ex:TimeoutCancellationException){row["error"]="timeout"}
            catch(ex:CancellationException){throw ex}
            catch(ex:Exception){row["error"]=ex.javaClass.simpleName+": "+ex.message}
            row["totalMs"]=android.os.SystemClock.elapsedRealtime()-start
            File(dir,"responses.jsonl").appendText(FlowJson.write(row)+"\n")
            android.util.Log.i("VixenSignatureSimulation","Saved $name ${row["totalMs"]}ms; review required")
        }
        result.putString("signature_ai_simulation","Generated quarantined evaluation; no test labels sent to model; semantic review required")
    } finally {(provider as? AutoCloseable)?.close()}
}
