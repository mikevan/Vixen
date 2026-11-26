package com.projectrevivesolutions.layertime.controller.state

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

object FieldObservationJournal {
    private val _health=MutableStateFlow(ObservationJournal.Health())
    val health=_health.asStateFlow()
    @Volatile private var journal:ObservationJournal?=null
    private var slices:com.projectrevivesolutions.layertime.controller.signatures.LiveEvidenceSlices?=null
    @Synchronized fun start(context:Context) {
        if(journal==null) {
            journal=ObservationJournal(File(context.filesDir,"field-journals"),onHealth={_health.value=it})
            slices=com.projectrevivesolutions.layertime.controller.signatures.LiveEvidenceSlices(emit={ fields ->
                journal?.record(if(fields["schema"]=="vixen-signature-attachment/1") "signature_attachment" else "signature_slice",System.currentTimeMillis(),System.nanoTime(),fields)
            })
        }
    }
    @Synchronized fun record(kind:String,fields:Map<String,Any?>):ObservationJournal.Receipt? {
        val monotonic=System.nanoTime()
        val receipt=journal?.record(kind,System.currentTimeMillis(),monotonic,fields) ?: return null
        if(fields.containsKey("hunterAddress") && fields.containsKey("sessionId")) slices?.accept(com.projectrevivesolutions.layertime.controller.signatures.LiveEvidenceSlices.Record(
            receipt.runId,receipt.sequence,monotonic/1000000,kind,fields.toMap(),receipt.queued))
        com.projectrevivesolutions.layertime.controller.metrics.KpiTelemetry.observe(kind,fields)
        return receipt
    }
    fun awaitIdle(timeoutMs:Long)=journal?.awaitIdle(timeoutMs)==true
    fun snapshot(destination:File):ObservationJournal.Health =
        requireNotNull(journal){"Field recording has not started."}.snapshot(destination)
    fun saveEvidenceOrigin(file:File, origin:com.projectrevivesolutions.layertime.controller.link.HunterEvidenceOrigin) {
        file.writeText(com.projectrevivesolutions.layertime.controller.flow.FlowJson.write(mapOf(
            "schema" to "hunter-evidence-origin/1","hunterAddress" to origin.hunterAddress,"sessionId" to origin.sessionId,
            "eventId" to origin.eventId,"receivedEpochMs" to origin.receivedEpochMs,"payloadSha256" to origin.payloadSha256,
            "pcapTimeBasis" to "synthetic_receipt_anchor","collecting" to origin.collecting)))
    }
}
