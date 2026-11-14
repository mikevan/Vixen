package com.projectrevivesolutions.layertime.controller
import android.content.Context
import android.os.Bundle
import com.projectrevivesolutions.layertime.controller.state.ObservationJournal
import com.projectrevivesolutions.layertime.controller.signatures.LiveEvidenceSlices
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import java.io.File

/** Isolated synthetic receipt replay on the device; never adds test data to field journals. */
internal fun runLiveSliceChecks(context:Context,result:Bundle) {
    val dir=File(context.filesDir,"diagnostics/signature-smoke-${java.util.UUID.randomUUID()}")
    val journal=ObservationJournal(dir)
    try {
        val slices=LiveEvidenceSlices({fields->journal.record("signature_slice",0,0,fields)})
        fun receive(time:Long,kind:String,count:Int=1) {
            val fields=mapOf("syntheticTest" to true,"hunterAddress" to "synthetic-only", "sessionId" to 1,
                "eventId" to 7L,"count" to count,"confidence" to 1,"ageSeconds" to 0)
            val receipt=journal.record(kind,0,time*1000000,fields)
            slices.accept(LiveEvidenceSlices.Record(receipt.runId,receipt.sequence,time,kind,fields,receipt.queued))
        }
        receive(1000,"status");receive(10000,"event_summary");receive(15000,"event_summary",2)
        receive(20001,"status");receive(22000,"evidence_received")
        check(journal.awaitIdle(10000))
        val health=journal.health();check(health.dropped==0L && health.failed==0L && health.pending==0L)
        val records=dir.listFiles().orEmpty().single().readLines().map {FlowJson.parse(it) as Map<*,*>}
        val slicesWritten=records.filter {it["kind"]=="signature_slice"}.map {it["fields"] as Map<*,*>}
        val closed=slicesWritten.single {it["state"]=="CLOSED"}
        check(closed["rawFramesIncluded"]==false && closed["coverageComplete"]==false)
        check((closed["records"] as List<*>).size==3)
        check(slicesWritten.any {it["schema"]=="vixen-signature-attachment/1"})
        result.putString("live_slices","PASS: isolated synthetic replay saved pre/post receipts and late attachment; no RF claim")
        result.putString("live_slices_artifact",dir.name)
    } finally {journal.close()}
}
