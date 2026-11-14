package com.projectrevivesolutions.layertime.controller.signatures

/** Receipt-clock evidence preservation, not a claim of raw RF capture or RF timing.
 * Called on the journal's serialized receipt path. emit must not call this recorder.
 */
class LiveEvidenceSlices(private val emit:(Map<String,Any?>)->Unit,
    private val windowMs:Long=10000,private val maxRecords:Int=512,private val maxPending:Int=8) {
    init {require(windowMs>0 && maxRecords>0 && maxPending>0)}
    data class Record(val run:String,val sequence:Long,val atMs:Long,val kind:String,
        val fields:Map<String,Any?>,val queued:Boolean,
        val clockDomain:String="phone-receipt-monotonic:$run",val timeBasis:String="phone_receipt_not_rf_observation")
    private data class Pending(val id:String,val trigger:Record,val start:Long,val end:Long,
        val records:MutableList<Record>,var truncated:Boolean=false,var revision:Int=0,var checkpointMs:Long=trigger.atMs)
    private val ring=ArrayDeque<Record>()
    private val pending=mutableListOf<Pending>()
    private val counts=linkedMapOf<String,Pair<Long,Int>>()
    private val savedSlices=linkedMapOf<String,MutableList<String>>()
    private var scope:String?=null
    private var lastMs:Long?=null
    private var overflowAt:Long?=null
    private fun scope(r:Record)="${r.run}/${r.fields["hunterAddress"]}/${r.fields["sessionId"]}/${r.clockDomain}"
    private fun snapshot(p:Pending,complete:Boolean,reason:String) {
        p.revision++
        emit(mapOf("schema" to "vixen-signature-slice/1","sliceId" to p.id,"revision" to p.revision,
            "state" to if(complete) "CLOSED" else "OPEN_OR_PARTIAL","reason" to reason,
            "runId" to p.trigger.run,"clockDomain" to p.trigger.clockDomain,
            "timeBasis" to p.trigger.timeBasis,"requestedStartMs" to p.start,
            "requestedEndMs" to p.end,"triggerSequence" to p.trigger.sequence,
            "hunterAddress" to p.trigger.fields["hunterAddress"],"sessionId" to p.trigger.fields["sessionId"],
            "rawFramesIncluded" to false,"modelInvoked" to false,"recordsTruncated" to p.truncated,
            "coverageComplete" to false,"firstPreservedReceiptMs" to p.records.firstOrNull()?.atMs,
            "lastPreservedReceiptMs" to p.records.lastOrNull()?.atMs,
            "knownChangeGap" to p.records.any {it.kind=="change_gap"},
            "unwrittenInputPossible" to p.records.any {!it.queued},
            "records" to p.records.map {mapOf("sequence" to it.sequence,"clockTimeMs" to it.atMs,"receivedMonotonicMs" to if(it.timeBasis=="phone_receipt_not_rf_observation")it.atMs else null,
                "kind" to it.kind,"journalQueued" to it.queued,"fields" to it.fields)},
            "limitations" to listOf("Receipt timing is not RF timing; no cross-device alignment is established.",
                "These are Hunter summaries and link records, not a continuous raw packet capture.",
                "Pre-trigger history may start late; queued journal entries are not proof of durable writes.",
                "No follow-on observation cannot establish safety or absence.")))
    }
    fun accept(r:Record) {
        require(r.atMs>=0 && r.sequence>0)
        val next=scope(r)
        // A late transfer from a former source remains in the main journal but
        // cannot close or contaminate the current source's live receipt window.
        if(r.kind.startsWith("evidence_") && scope!=null && next!=scope)return
        if(scope!=null && (scope!=next || lastMs?.let {r.atMs<it}==true)) {
            pending.forEach {snapshot(it,false,"source_session_or_clock_changed")}
            pending.clear();ring.clear();counts.clear();savedSlices.clear();overflowAt=null
        }
        scope=next;lastMs=r.atMs
        val iterator=pending.iterator()
        while(iterator.hasNext()) {
            val p=iterator.next()
            if(r.atMs>p.end) {snapshot(p,true,"post_trigger_interval_elapsed");iterator.remove()}
            else {
                if(p.records.size<maxRecords) p.records.add(r) else p.truncated=true
                if(r.atMs-p.checkpointMs>=1000) {snapshot(p,false,"evidence_updated");p.checkpointMs=r.atMs}
            }
        }
        while(ring.isNotEmpty() && r.atMs-ring.first().atMs>windowMs)ring.removeFirst()
        if(ring.size>=maxRecords) {overflowAt=r.atMs;ring.removeFirst()}
        ring.addLast(r)
        if(r.kind in setOf("evidence_received","evidence_save_failed","evidence_transfer_failed","evidence_auto_skipped")) {
            val key="${r.fields["hunterAddress"]}/${r.fields["sessionId"]}/${r.fields["eventId"]}"
            savedSlices[key]?.forEach {id->emit(mapOf("schema" to "vixen-signature-attachment/1",
                "sliceId" to id,"runId" to r.run,"recordKind" to r.kind,"evidenceSequence" to r.sequence,"journalQueued" to r.queued,
                "fields" to r.fields,"timeBasis" to "separate_packet_window_not_aligned_rf_time"))}
        }
        if(r.kind!="event_summary")return
        val f=r.fields
        val address=f["hunterAddress"] as? String ?: return
        val session=(f["sessionId"] as? Number)?.toLong() ?: return
        val event=(f["eventId"] as? Number)?.toLong() ?: return
        val confidence=(f["confidence"] as? Number)?.toInt() ?: return
        val age=(f["ageSeconds"] as? Number)?.toLong() ?: return
        val count=(f["count"] as? Number)?.toLong() ?: return
        if(address.isBlank() || session !in 1..65535 || age !in 0..3 || count<=0)return
        val key="$address/$session/$event"
        val previous=counts[key];counts[key]=maxOf(count,previous?.first ?: 0L) to maxOf(confidence,previous?.second ?: 0)
        if(counts.size>maxRecords)counts.remove(counts.keys.first())
        if(confidence !in 1..2 || previous?.let {count<=it.first && confidence<=it.second}==true)return
        // Additional reports for the same pending event extend its evidence, not
        // its deadline. A later trigger can preserve the next overlapping slice.
        if(pending.any {it.trigger.fields["eventId"]==event && it.trigger.fields["hunterAddress"]==address})return
        if(pending.size>=maxPending) {val oldest=pending.removeAt(0);snapshot(oldest,false,"pending_capacity_reached")}
        val start=maxOf(0,r.atMs-windowMs)
        val end=if(Long.MAX_VALUE-r.atMs<windowMs) Long.MAX_VALUE else r.atMs+windowMs
        val p=Pending("${r.run}:${r.sequence}",r,start,end,ring.toMutableList(),
            overflowAt?.let {it>=start}==true)
        val ids=savedSlices.getOrPut(key){mutableListOf()};ids.add(p.id)
        if(ids.size>32)ids.removeAt(0)
        if(savedSlices.size>maxRecords)savedSlices.remove(savedSlices.keys.first())
        pending.add(p);snapshot(p,false,"procedural_fresh_medium_or_high_confidence_trigger")
    }
}
