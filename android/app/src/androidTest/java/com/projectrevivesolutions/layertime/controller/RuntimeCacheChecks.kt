package com.projectrevivesolutions.layertime.controller
import android.content.Context
import android.os.Bundle
import com.projectrevivesolutions.layertime.controller.ai.*
import com.projectrevivesolutions.layertime.controller.ai.runtime.BundledModelFactory
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import kotlinx.coroutines.*
import java.io.File
internal fun runRuntimeCacheChecks(context:Context,result:Bundle,label:String="build128")=runBlocking {
    check(BuildConfig.DEBUG && label.matches(Regex("[a-z0-9-]{1,60}")))
    val input=FlowJson.parse(File(context.cacheDir,"field-collection-evaluation.json").readText()) as Map<*,*>
    val cases=input["cases"] as List<*>;check(cases.size>=2)
    fun snapshot(value:Any?):FieldShadowTasks.Snapshot {
        val m=value as Map<*,*>;return FieldShadowTasks.Snapshot(m["runId"] as String,m["summarySequence"] as Long,
            m["hunterAddress"] as String,(m["sessionId"] as Long).toInt(),m["eventId"] as Long,m["summaryReceivedEpochMs"] as Long,
            (m["facts"] as Map<*,*>).entries.associate {it.key as String to it.value})
    }
    val a=snapshot(cases[0]);val b=snapshot(cases[1]);check(a.facts["reportedSightings"]!=b.facts["reportedSightings"])
    val provider=BundledModelFactory.development(context)
    val file=File(context.filesDir,"diagnostics/runtime-cache-$label.jsonl");check(!file.exists())
    suspend fun generate(label:String,s:FieldShadowTasks.Snapshot):String {
        val request=FieldShadowTasks.request(s);check(AiCoordinator(provider).execute(request){it} is AiOutcome.Unavailable)
        val start=android.os.SystemClock.elapsedRealtime();val tokens=withTimeout(120000){provider.countInputTokens(request)}
        val generated=android.os.SystemClock.elapsedRealtime();val response=withTimeout(120000){provider.generate(request)}
        val problems=FieldShadowTasks.reportProblems(s,FieldShadowTasks.decode(response.json))
        val row=mapOf("label" to label,"snapshotSha256" to s.sha256,"inputTokens" to tokens,
            "loadTokenizeMs" to generated-start,"generationMs" to android.os.SystemClock.elapsedRealtime()-generated,
            "output" to response.json,"requiredReportFactProblems" to problems,"releaseEligible" to false)
        file.parentFile?.mkdirs();file.appendText(FlowJson.write(row)+"\n");check(problems.isEmpty());return response.json
    }
    try {
        val cold=generate("cold",a);val same=generate("same-prompt",a);check(cold==same) {"Cache changed the greedy answer"}
        generate("changed-report-count",b)
        val start=android.os.SystemClock.elapsedRealtime()
        check(withTimeoutOrNull(100){provider.generate(FieldShadowTasks.request(a).copy(maxOutputTokens=1024))}==null)
        withTimeout(30000){provider.countInputTokens(FieldShadowTasks.request(a))}
        val cancelledMs=android.os.SystemClock.elapsedRealtime()-start
        val after=generate("after-cancellation",a);check(cold==after) {"Cancelled KV state changed the answer"}
        file.appendText(FlowJson.write(mapOf("label" to "checks","sameAnswer" to true,"changedCountCorrect" to true,
            "afterCancellationSameAnswer" to true,"cancelAndDrainMs" to cancelledMs,
            "processPssKiB" to android.os.Debug.MemoryInfo().also {android.os.Debug.getMemoryInfo(it)}.totalPss))+"\n")
        result.putString("runtime_cache","PASS: identical output, changed count, cancellation and recovery")
    } finally {(provider as? AutoCloseable)?.close()}
}
