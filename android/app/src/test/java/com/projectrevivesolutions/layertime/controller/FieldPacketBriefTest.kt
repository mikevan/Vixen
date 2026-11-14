package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.ai.*
import com.projectrevivesolutions.layertime.controller.ai.runtime.LocalPromptFormat
import org.junit.Assert.*
import org.junit.Test
class FieldPacketBriefTest {
    private fun facts()=FieldPacketBrief.Facts("a".repeat(64),64,103,39,18,0,0)
    @Test fun supplied_counts_and_loss_survive_a_compact_model_prompt() {
        val request=FieldPacketBrief.request(facts())
        val text=LocalPromptFormat.render(request,LocalPromptFormat.COMPACT_2507)
        for(value in listOf("saved 64 of 103","18 deauthentication","0 EAPOL","dropped 39","partial saved window"))assertTrue(text.contains(value))
        assertEquals(256,request.maxOutputTokens);assertTrue(request.tools.isEmpty())
        assertEquals(AiTask.EXPLAIN_EVIDENCE,request.task)
    }
    @Test fun changed_packet_counts_are_rejected_even_when_json_is_valid() {
        val correct=FieldShadowTasks.Explanation("A partial packet window",
            "Hunter saved 64 of 103 observed frames and dropped 39. The saved window contains 18 deauthentication frames and 0 EAPOL frames.",
            "Incomplete coverage cannot establish no reconnect or safety.","Preserve the capture.")
        assertTrue(FieldPacketBrief.reportProblems(facts(),correct).isEmpty())
        assertFalse(FieldPacketBrief.reportProblems(facts(),correct.copy(significance=correct.significance.replace("saved 64","saved 66"))).isEmpty())
        assertFalse(FieldPacketBrief.reportProblems(facts(),correct.copy(significance=correct.significance.replace("saved 64","saved 39"))).isEmpty())
        assertFalse(FieldPacketBrief.reportProblems(facts(),correct.copy(limitations="The recording lasted 15 seconds.")).isEmpty())
    }
    @Test fun contradictory_counts_cannot_be_supplied_as_verified_facts() {
        for(f in listOf({facts().copy(droppedFrames=38)},{facts().copy(deauthFrames=65)},
            {facts().copy(eapolFrames=-1)},{facts().copy(sourceSha256="unknown")})) {
            assertThrows(IllegalArgumentException::class.java){f()}
        }
    }
}
