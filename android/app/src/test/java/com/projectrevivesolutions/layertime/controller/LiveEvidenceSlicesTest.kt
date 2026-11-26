package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.signatures.LiveEvidenceSlices
import org.junit.Assert.*
import org.junit.Test
class LiveEvidenceSlicesTest {
    private fun record(seq:Long,time:Long,kind:String="event_summary",count:Long=1,session:Int=1,confidence:Int=1,age:Int=0)=
        LiveEvidenceSlices.Record("run",seq,time,kind,mapOf("hunterAddress" to "hunter", "sessionId" to session,
            "eventId" to 7L,"count" to count,"confidence" to confidence,"ageSeconds" to age),true)
    @Test fun live_trigger_preserves_pre_and_post_receipts_and_closes_on_heartbeat() {
        val output=mutableListOf<Map<String,Any?>>()
        val recorder=LiveEvidenceSlices({output.add(it)})
        recorder.accept(record(1,1000,"status"));recorder.accept(record(2,10000))
        recorder.accept(record(3,15000,count=2));recorder.accept(record(4,20001,"status"))
        val closed=output.last()
        assertEquals("CLOSED",closed["state"]);assertEquals(false,closed["rawFramesIncluded"])
        assertEquals(0L,closed["requestedStartMs"]);assertEquals(20000L,closed["requestedEndMs"])
        val records=closed["records"] as List<*>
        assertEquals(listOf(1L,2L,3L),records.map {(it as Map<*,*>)["sequence"]})
        assertEquals(1,output.map {it["sliceId"]}.distinct().size)
    }
    @Test fun stale_low_confidence_and_replayed_summaries_do_not_trigger() {
        val output=mutableListOf<Map<String,Any?>>()
        val recorder=LiveEvidenceSlices({output.add(it)})
        recorder.accept(record(1,1000,confidence=0));recorder.accept(record(2,2000,age=4))
        assertTrue(output.isEmpty())
        recorder.accept(record(3,3000,confidence=2));recorder.accept(record(4,14001,"status"))
        val before=output.size;recorder.accept(record(5,15000,confidence=2))
        assertEquals(before,output.size)
    }
    @Test fun session_change_preserves_partial_slice_without_joining_new_session() {
        val output=mutableListOf<Map<String,Any?>>()
        val recorder=LiveEvidenceSlices({output.add(it)})
        recorder.accept(record(1,1000));recorder.accept(record(2,2000,"status",session=2))
        assertEquals("source_session_or_clock_changed",output.last()["reason"])
        assertEquals("OPEN_OR_PARTIAL",output.last()["state"])
        assertEquals(1,(output.last()["records"] as List<*>).size)
    }
    @Test fun packet_evidence_can_attach_after_summary_window_closes() {
        val output=mutableListOf<Map<String,Any?>>()
        val recorder=LiveEvidenceSlices({output.add(it)})
        recorder.accept(record(1,1000));recorder.accept(record(2,12000,"status"))
        recorder.accept(record(3,15000,"evidence_received"))
        assertEquals("vixen-signature-attachment/1",output.last()["schema"])
        assertEquals("run:1",output.last()["sliceId"])
    }
    @Test fun capacity_loss_is_explicit_and_does_not_stop_live_input() {
        val output=mutableListOf<Map<String,Any?>>()
        val recorder=LiveEvidenceSlices({output.add(it)},maxRecords=2,maxPending=1)
        recorder.accept(record(1,1,"status"));recorder.accept(record(2,2,"status"));recorder.accept(record(3,3))
        assertEquals(true,output.last()["recordsTruncated"])
        recorder.accept(record(4,20000,"status"))
        assertEquals("CLOSED",output.last()["state"])
    }
}
