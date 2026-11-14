package com.projectrevivesolutions.layertime.controller.ai

import com.projectrevivesolutions.layertime.controller.BuildConfig
import com.projectrevivesolutions.layertime.controller.state.FieldObservationJournal
import kotlinx.coroutines.*

object FieldShadowExperiment {
    data class Result(val explanation:FieldShadowTasks.Explanation?,val raw:String,val milliseconds:Long,val reportProblems:List<String> = emptyList())
    suspend fun run(provider:LocalModelProvider,input:FieldShadowTasks.Snapshot):Result {
        check(BuildConfig.DEBUG) {"Field model experiments require a diagnostic build."}
        val manifest=requireNotNull(provider.availability().manifest)
        check(!manifest.blueTeamTrained && AiTask.EXPLAIN_EVIDENCE in manifest.tasks)
        val request=FieldShadowTasks.request(input)
        val trace=mapOf("snapshotId" to input.id,"snapshotSha256" to input.sha256,"sourceJson" to input.sourceJson,
            "hunterAddress" to input.hunterAddress,"sessionId" to input.sessionId,"eventId" to input.eventId,
            "inputRunId" to input.runId,"inputSummarySequence" to input.summarySequence,
            "modelId" to manifest.id,"modelVersion" to manifest.version,"weightsSha256" to manifest.sha256,
            "releaseEligible" to false,"trainingApproved" to false,"semanticReviewRequired" to true)
        val start=System.nanoTime()
        val receipt=requireNotNull(FieldObservationJournal.record("model_shadow_request",trace + mapOf(
            "messages" to request.messages.map {mapOf("role" to it.role.name,"content" to it.content)},
            "maxOutputTokens" to request.maxOutputTokens)))
        check(receipt.queued && withContext(Dispatchers.IO) {FieldObservationJournal.awaitIdle(5000)} &&
            FieldObservationJournal.health.value.lastWrittenSequence>=receipt.sequence) {"The field input could not be saved for review."}
        try {
            val response=withTimeout(60_000) {
                val tokens=provider.countInputTokens(request)
                check(tokens>=0 && tokens.toLong()+request.maxOutputTokens<=manifest.contextTokens)
                provider.generate(request)
            }
            val elapsed=(System.nanoTime()-start)/1_000_000L
            val identityValid=response.modelId==manifest.id && response.modelVersion==manifest.version && response.toolCalls.isEmpty()
            val parsed=if(identityValid)runCatching {FieldShadowTasks.decode(response.json)}.getOrNull() else null
            val problems=parsed?.let {FieldShadowTasks.reportProblems(input,it)} ?: emptyList()
            val answer=parsed?.takeIf {problems.isEmpty()}
            FieldObservationJournal.record("model_shadow_response",trace + mapOf("requestSequence" to receipt.sequence,
                "output" to response.json.take(32000),"outputTruncated" to (response.json.length>32000),
                "milliseconds" to elapsed,"syntaxAndReferencesValid" to (parsed!=null),"requiredReportFactProblems" to problems,"modelIdentityValid" to identityValid))
            return Result(answer,response.json.take(32000),elapsed,problems)
        } catch(ex:TimeoutCancellationException) {
            FieldObservationJournal.record("model_shadow_timeout",trace+mapOf("requestSequence" to receipt.sequence));throw ex
        } catch(ex:CancellationException) {
            FieldObservationJournal.record("model_shadow_cancelled",trace+mapOf("requestSequence" to receipt.sequence));throw ex
        } catch(ex:Exception) {
            FieldObservationJournal.record("model_shadow_failed",trace+mapOf("requestSequence" to receipt.sequence));throw ex
        }
    }
}
