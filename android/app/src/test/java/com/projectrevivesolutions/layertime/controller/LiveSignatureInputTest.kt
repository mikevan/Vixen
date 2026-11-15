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
}
