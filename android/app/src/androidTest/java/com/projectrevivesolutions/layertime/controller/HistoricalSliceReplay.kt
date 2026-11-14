package com.projectrevivesolutions.layertime.controller
import android.content.Context
import android.os.Bundle
import com.projectrevivesolutions.layertime.controller.signatures.*
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import java.io.File
import java.security.MessageDigest

/** Original field receipts replayed offline. Never calls LinkClient or a model. */
internal fun runHistoricalSliceReplay(context:Context,result:Bundle) {
    val input=File(context.cacheDir,"historical-slice-replay.jsonl")
    val bytes=input.readBytes();check(bytes.size in 1..20000000)
    val sourceSha=MessageDigest.getInstance("SHA-256").digest(bytes).joinToString(""){"%02x".format(it)}
    val source=bytes.toString(Charsets.UTF_8).lineSequence().filter {it.isNotBlank()}.map {FlowJson.parse(it) as Map<*,*>}.toList()
    val directory=File(context.filesDir,"diagnostics/historical-slice-replay-${java.util.UUID.randomUUID()}").apply {check(mkdirs())}
    val snapshots=mutableListOf<Map<String,Any?>>()
    val recorder=LiveEvidenceSlices({fields->
        snapshots.add(fields)
        File(directory,"slices.jsonl").appendText(FlowJson.write(mapOf("replay" to true,"sourceSha256" to sourceSha,"fields" to fields))+"\n")
    })
    val policy=EvidenceCapturePolicy();val requests=mutableListOf<Long>();var eligible=0
    val originals=source.associateBy {it["sequence"] as Long}
    for(row in source) {
        val raw=row["fields"] as Map<*,*>
        if(!raw.containsKey("hunterAddress") || !raw.containsKey("sessionId"))continue
        val fields=raw.entries.associate {it.key as String to it.value}
        val at=(row["receivedMonotonicNs"] as Long)/1000000
        recorder.accept(LiveEvidenceSlices.Record(row["runId"] as String,row["sequence"] as Long,at,row["kind"] as String,fields,true))
        if(row["kind"]=="event_summary") {
            val confidence=(raw["confidence"] as Long).toInt();val age=(raw["ageSeconds"] as Long).toInt()
            if(confidence in 1..2 && age in 0..3 && (raw["count"] as Long)>0)eligible++
            if(raw["detector"]==5L && policy.shouldCapture(raw["hunterAddress"] as String,
                (raw["sessionId"] as Long).toInt(),raw["eventId"] as Long,(raw["count"] as Long).toInt(),confidence,age,at))
                requests.add(row["sequence"] as Long)
        }
    }
    val slices=snapshots.filter {it["schema"]=="vixen-signature-slice/1"}.groupBy {it["sliceId"]}
    check(eligible==1 && slices.size==1 && requests.size==1) {"Historical scenario changed; review original receipts"}
    val closed=slices.values.single().last();check(closed["state"]=="CLOSED")
    check(closed["recordsTruncated"]==false && closed["knownChangeGap"]==false)
    val records=closed["records"] as List<*>
    for(record in records) {
        val preserved=record as Map<*,*>;val original=originals[preserved["sequence"]] ?: error("Invented receipt")
        check(preserved["fields"]==original["fields"])
        check((preserved["receivedMonotonicMs"] as Long) in (closed["requestedStartMs"] as Long)..(closed["requestedEndMs"] as Long))
        val fields=original["fields"] as Map<*,*>
        check(fields["hunterAddress"]==closed["hunterAddress"] && fields["sessionId"]==closed["sessionId"])
    }
    val report=mapOf("schema" to "historical-slice-replay/1","replay" to true,"sourceSha256" to sourceSha,
        "inputRecords" to source.size,"eligibleTriggerRecords" to eligible,"preservedSlices" to slices.size,
        "preservedReceiptCount" to records.size,"closed" to true,"originalFactsPreserved" to true,
        "simulatedNativeCaptureTriggerSequences" to requests,"realHunterCommandsSent" to 0,"modelInvoked" to false,
        "limits" to listOf("Replays previously received facts; does not recreate RF or test BLE transfer concurrency.",
            "Low-confidence tracker reports remain in context but do not trigger the current medium/high policy.",
            "No drone observation was supplied; no completed attack signature is established."))
    File(directory,"report.json").writeText(FlowJson.write(report))
    result.putString("historical_replay","PASS: original field facts produced one closed slice and one simulated packet trigger; no live commands")
    result.putString("historical_replay_artifact",directory.name)
}
