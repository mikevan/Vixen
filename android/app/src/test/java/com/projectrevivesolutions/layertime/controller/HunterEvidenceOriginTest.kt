package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.link.*
import org.junit.Assert.*
import org.junit.Test
class HunterEvidenceOriginTest {
    private val peer="AA:BB:CC:DD:EE:FF"
    @Test fun source_identity_is_not_just_a_file_name_or_event_number() {
        val o=HunterEvidenceOrigin(peer,17,9,1234,"a".repeat(64),false)
        assertTrue(o.matches(17,9,peer.lowercase()))
        assertFalse(o.matches(18,9,peer));assertFalse(o.matches(17,10,peer))
        assertFalse(o.matches(17,9,"11:22:33:44:55:66"));assertFalse(o.matches(17,9,null))
    }
    @Test fun payload_digest_ignores_synthetic_times_and_radio_wrappers_but_not_frame_boundaries() {
        fun frame(index:Int,bytes:ByteArray)=LinkEvidence.Frame(LinkEvidence.Index(1,index,6,-50,bytes.size,bytes.size,0,0),bytes)
        val one=frame(0,byteArrayOf(1,2))
        assertEquals(LinkEvidence.payloadHash(listOf(one)),LinkEvidence.payloadHash(listOf(one.copy(index=one.index.copy(channel=11,rssi=-80,offsetMs=-100)))))
        assertNotEquals(LinkEvidence.payloadHash(listOf(one)),LinkEvidence.payloadHash(listOf(frame(0,byteArrayOf(1)),frame(1,byteArrayOf(2)))))
        assertNotEquals(LinkEvidence.payloadHash(listOf(one)),LinkEvidence.payloadHash(listOf(frame(0,byteArrayOf(1,3)))))
    }
    @Test fun partial_payload_cannot_receive_a_complete_digest() {
        val i=LinkEvidence.Index(1,0,6,-50,2,2,0,0)
        try {LinkEvidence.payloadHash(listOf(LinkEvidence.Frame(i,byteArrayOf(1)))) ;fail("Partial frame accepted")}
        catch(expected:IllegalArgumentException) {}
    }
    @Test fun a_different_hunter_with_the_same_session_cannot_inherit_the_mirror() {
        val repo=com.projectrevivesolutions.layertime.controller.state.ControllerRepository
        repo.reset()
        repo.setDeviceLabel("first",peer)
        repo.onServerInfo(Frame.HelloAck(1,0,0,2,17,0,20))
        repo.onEventSummary(Frame.EventSummary(1,0,7,5,2,1,1,6,-50,1,0,1))
        repo.onEventText(7,0,"11:22:33:44:55:66")
        assertEquals(1,repo.eventCount())
        assertNotNull(repo.latestFieldAnalysis.value)
        repo.onServerInfo(Frame.HelloAck(1,0,0,2,18,0,20))
        assertNull(repo.latestFieldAnalysis.value)
        repo.setDeviceLabel("second","11:22:33:44:55:77")
        repo.onServerInfo(Frame.HelloAck(1,0,0,2,17,0,20))
        assertEquals(0,repo.eventCount())
        repo.reset()
    }
}
