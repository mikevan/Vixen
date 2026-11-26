package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.signatures.LiveSignatureInput
import com.projectrevivesolutions.layertime.controller.model.EventRow
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import org.junit.Assert.*
import org.junit.Test
class LiveSignatureInputTest {
    private val rid="RID1:0D00021254"+"00".repeat(22)
    private fun row(id:Long,detector:Int,time:Long?,source:String="AA:BB:CC:DD:EE:01",producer:String="hunter",session:Int=1)=EventRow(
        sessionId=session,eventId=id,detector=detector,detectorName="observed",confidence=1,sourceKind=if(detector==19)2 else 1,
        band=1,channel=if(detector==19)0 else 6,rssi=-50,count=1,ageSeconds=0,hasSourceId=true,hasDetail=true,
        sourceId=source,detail=if(detector==19)rid else "EAPOL handshake M1",firstSeenMillis=id,lastUpdatedMillis=id,
        producerAddress=producer,sourceFirstUptimeMs=time,sourceLastUptimeMs=time)
    private fun rows()=listOf(row(1,5,20000),row(2,18,32000),row(3,19,50000))
    @Test fun source_clock_gap_and_references_survive() {
        val input=requireNotNull(LiveSignatureInput.prepare(rows()));assertEquals(30000L,input.gapMs)
        val value=FlowJson.parse(input.json) as Map<*,*>;val rel=value["relations"] as Map<*,*>
        assertEquals(true,rel["withinWindow"]);assertEquals(true,rel["clockAligned"])
        assertEquals(listOf(1L,2L,3L),input.records.map {it.eventId})
    }
    @Test fun no_generic_drone_label_or_uuid_substitution() {
        assertTrue(LiveSignatureInput.validRemoteId(rid));assertFalse(LiveSignatureInput.validRemoteId("Drone"))
        assertNull(LiveSignatureInput.prepare(rows().map {if(it.detector==19)it.copy(detail="0xFFFA") else it}))
        assertFalse(LiveSignatureInput.validRemoteId(rid.dropLast(2)))
    }
    @Test fun sources_sessions_and_networks_never_borrow_stages() {
        for(changed in listOf(row(3,19,50000,producer="other"),row(3,19,50000,session=2)))
            assertNull(LiveSignatureInput.prepare(rows().take(2)+changed))
        assertNull(LiveSignatureInput.prepare(listOf(row(1,5,20000),row(2,18,32000,source="different"),row(3,19,50000))))
    }
    @Test fun absent_source_time_is_not_receipt_time() {
        val input=requireNotNull(LiveSignatureInput.prepare(rows().map {it.copy(sourceFirstUptimeMs=null)}))
        assertNull(input.gapMs);val rel=(FlowJson.parse(input.json) as Map<*,*>)["relations"] as Map<*,*>
        assertEquals(false,rel["clockAligned"]);assertNull(rel["withinWindow"])
    }
    @Test fun source_uptime_wrap_preserves_order() {
        val input=requireNotNull(LiveSignatureInput.prepare(listOf(row(1,5,0xfffff000L),row(2,18,0x1000),row(3,19,0x3000))))
        assertEquals(0x4000L,input.gapMs)
    }
    @Test fun incomplete_sequence_does_not_invoke_model() {assertNull(LiveSignatureInput.prepare(rows().take(2)))}
    // Regression derived from the preserved failed T2 session. Acceptance only;
    // these observations are not added to model training or a phone collection.
    private fun t2()=listOf(
        row(1,5,57372,source="02:00:00:00:01:01").copy(count=8),
        row(2,18,74372,source="02:00:00:00:01:01").copy(count=4),
        row(3,5,92372,source="02:00:00:00:01:03").copy(count=8),
        row(4,18,102372,source="02:00:00:00:01:04").copy(count=4),
        row(5,19,119372,source="02:00:00:00:01:02").copy(count=4))
    @Test fun unrelated_newer_networks_do_not_hide_the_valid_pair_from_failed_t2() {
        val input=requireNotNull(LiveSignatureInput.prepare(t2()))
        assertEquals(listOf(1L,2L,5L),input.records.map {it.eventId})
        assertEquals(listOf(8,4,4),input.records.map {it.count})
        assertEquals(62000L,input.gapMs)
        val relations=(FlowJson.parse(input.json) as Map<*,*>)["relations"] as Map<*,*>
        assertEquals(true,relations["sameNetworkDisconnectExchange"])
        assertEquals(true,relations["withinWindow"])
    }
    @Test fun repeated_delivery_and_list_order_do_not_change_t2_selection() {
        val original=requireNotNull(LiveSignatureInput.prepare(t2()))
        val repeated=requireNotNull(LiveSignatureInput.prepare((t2()+t2()).reversed()))
        assertEquals(original.key,repeated.key);assertEquals(original.json,repeated.json)
    }
    @Test fun unrelated_networks_cannot_create_a_pair_when_a_is_missing() {
        assertNull(LiveSignatureInput.prepare(t2().filter {it.eventId !in setOf(1L,2L)}))
        assertNull(LiveSignatureInput.prepare(t2().filter {it.eventId!=2L}))
    }
    @Test fun matched_pairs_still_preserve_negative_temporal_evidence() {
        val b=listOf(row(6,5,100000,source="B"),row(7,18,110000,source="b"),row(8,19,90000))
        val input=requireNotNull(LiveSignatureInput.prepare(b+row(9,5,120000,source="unmatched")))
        assertEquals(listOf(6L,7L,8L),input.records.map {it.eventId})
        assertEquals(-10000L,input.gapMs)
        val relations=(FlowJson.parse(input.json) as Map<*,*>)["relations"] as Map<*,*>
        assertEquals(false,relations["remoteIdAfterExchange"])
    }

}
