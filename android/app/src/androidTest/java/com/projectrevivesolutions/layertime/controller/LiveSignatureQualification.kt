package com.projectrevivesolutions.layertime.controller
import android.content.Context
import android.os.Bundle
import com.projectrevivesolutions.layertime.controller.ai.*
import com.projectrevivesolutions.layertime.controller.ai.runtime.*
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest

/** Owner-authorized separate test APK. No RF, collection injection or app UI controls. */
internal fun runLiveSignatureQualification(app:Context,fixtures:Context,result:Bundle,production:Boolean)=runBlocking {
    fun asset(name:String)=app.assets.open("ai/production/$name").bufferedReader().use {it.readText()}
    fun sha(file:File):String {
        val digest=MessageDigest.getInstance("SHA-256")
        file.inputStream().use {stream->val buffer=ByteArray(1024*1024);while(true){val n=stream.read(buffer);if(n<0)break;digest.update(buffer,0,n)}}
        return digest.digest().joinToString(""){"%02x".format(it)}
    }
    val data=FlowJson.parse(asset("package.json")) as Map<*,*>
    val manifest=LocalModelManifest(data["modelId"] as String,data["modelVersion"] as String,data["sha256"] as String,1,
        setOf(AiTask.INTERPRET_SIGNATURE),setOf(ModelCapability.STRUCTURED_OUTPUT),2048,false)
    val dir=File(app.filesDir,"diagnostics/quarantined-signature-${java.util.UUID.randomUUID()}").apply {check(mkdirs())}
    val weights=File(app.filesDir,"ai-models/${manifest.sha256}/weights.gguf")
    if(!weights.isFile) {weights.parentFile!!.mkdirs();app.assets.open("ai/production/weights.gguf").use {input->weights.outputStream().use {input.copyTo(it)}}}
    check(sha(weights)==manifest.sha256)
    val schema=asset("signature-schema.json");val system=asset("signature-system.txt")
    val provider=if(production)BundledModelFactory.production(app) else NativeLlamaProvider(manifest,{weights},
        app.assets.open("ai/json.gbnf").use {it.readBytes()},developmentOnly=true,promptFormat=LocalPromptFormat.QWEN3_COMPACT,
        signatureGrammar=app.assets.open("ai/production/signature.gbnf").use {it.readBytes()})
    val cases=FlowJson.parse(fixtures.assets.open("model-evaluations/live-signature-v3.json").bufferedReader().use {it.readText()}) as List<*>
    val expanded=cases.toMutableList()
    fun sourceRow(id:Long,detector:Int,at:Long?)=com.projectrevivesolutions.layertime.controller.model.EventRow(
        sessionId=1,eventId=id,detector=detector,detectorName="observed",confidence=1,sourceKind=if(detector==19)2 else 1,band=1,channel=if(detector==19)0 else 6,
        rssi=-50,count=1,ageSeconds=0,hasSourceId=true,hasDetail=true,sourceId=if(detector==19)"02:00:00:00:00:02" else "02:00:00:00:00:01",
        detail=if(detector==19)"RID1:0D00021254"+"00".repeat(22) else "EAPOL handshake M1",firstSeenMillis=id,lastUpdatedMillis=id,
        producerAddress="hunter",sourceFirstUptimeMs=at,sourceLastUptimeMs=at)
    for((label,remote,expected) in listOf(Triple("normal-positive",50000L,"disconnect_sequence_then_remote_id"),Triple("normal-reverse",10000L,"reverse_order"),Triple("normal-late",120001L,"outside_window"))) {
        val input=requireNotNull(com.projectrevivesolutions.layertime.controller.signatures.LiveSignatureInput.prepare(listOf(sourceRow(1,5,20000),sourceRow(2,18,32000),sourceRow(3,19,remote))))
        expanded+=mapOf("id" to label,"input" to FlowJson.parse(input.json),"expected" to mapOf("pattern" to expected))
    }
    val rows=mutableListOf<Map<String,Any?>>()
    try {
        val coordinator=AiCoordinator(provider)
        for(value in expanded) {
            val case=value as Map<*,*>;val input=case["input"] as Map<*,*>
            fun canonical(v:Any?):Any?=when(v){is Map<*,*>->v.entries.associate {it.key.toString() to canonical(it.value)}.toSortedMap();is List<*>->v.map {canonical(it)};else->v}
            val request=LocalModelRequest(AiTask.INTERPRET_SIGNATURE,listOf(PromptMessage(PromptMessage.Role.SYSTEM,system),
                PromptMessage(PromptMessage.Role.USER,FlowJson.write(canonical(input)))),schema,64)
            val start=System.nanoTime();var raw:String?=null;var tokens:Int?=null
            val outcome=if(production)coordinator.execute(request,120000){text->raw=text;FlowJson.parse(text)} else withTimeout(120000) {
                tokens=provider.countInputTokens(request);check(tokens!!+64<=2048)
                val response=provider.generate(request);raw=response.json
                AiOutcome.Completed(FlowJson.parse(response.json),response.modelId,response.modelVersion)
            }
            val correct=outcome is AiOutcome.Completed && outcome.value==case["expected"]
            rows+=mapOf("id" to case["id"],"rawOutput" to raw,"correct" to correct,"seconds" to (System.nanoTime()-start)/1e9,"inputTokens" to tokens)
            File(dir,"results.json").writeText(FlowJson.write(mapOf("modelSha256" to manifest.sha256,"productionCoordinator" to production,
                "total" to expanded.size,"completed" to rows.size,"correct" to rows.count {it["correct"]==true},"results" to rows)))
            android.util.Log.i("VixenSignatureQualification","case=${case["id"]} correct=$correct ${rows.size}/${expanded.size}")
        }
        result.putString("signature_qualification_file",dir.absolutePath)
        result.putString("signature_qualification","${rows.count {it["correct"]==true}}/${expanded.size}; production=$production")
        check(rows.all {it["correct"]==true}) {"Signature qualification failed; exact outputs preserved"}
    } finally {if(!production)(provider as? AutoCloseable)?.close()}
}
