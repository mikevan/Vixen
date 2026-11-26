package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.signatures.AttackSignatures.Signature
import com.projectrevivesolutions.layertime.controller.signatures.AttackSignatures.Observation
import com.projectrevivesolutions.layertime.controller.signatures.AttackSignatures.Slice
import com.projectrevivesolutions.layertime.controller.signatures.AttackSignatures.State
import com.projectrevivesolutions.layertime.controller.signatures.AttackSignatures
import org.junit.Assert.*
import org.junit.Test
class AttackSignaturesTest {
    private val signature=Signature("flood-follow-on",1,"flood","drone-observation",90000)
    private fun event(id:String,kind:String,time:Long)=Observation(id,kind,time,"test-source","evidence:$id")
    private fun slice(id:String,start:Long,end:Long,vararg events:Observation,run:String="run",clock:String="aligned")=
        Slice(id,run,clock,start,end,events.toList(),listOf("Test coverage only"))
    private fun assess(vararg slices:Slice)=AttackSignatures.assess(signature,slices.toList())
    @Test fun ai_request_preserves_scope_and_uncertainty_without_invoking_a_model() {
        val a=assess(slice("a",0,10000,event("f","flood",1000))).single()
        val request=com.projectrevivesolutions.layertime.controller.signatures.SignatureBrief.request(a)
        val text=request.messages.joinToString {it.content}
        for(value in listOf("CANDIDATE","clockDomain","evidence:f","Draft signature","not a confirmed attack"))
            assertTrue(value,text.contains(value))
        assertEquals(com.projectrevivesolutions.layertime.controller.ai.AiTask.EXPLAIN_EVIDENCE,request.task)
        assertTrue(request.tools.isEmpty())
    }
    @Test fun bundled_catalog_is_versioned_draft_and_cannot_self_qualify() {
        val json=java.io.File("src/main/assets/signatures/library-v1.json").readText()
        val catalog=com.projectrevivesolutions.layertime.controller.signatures.SignatureCatalog
        val entry=catalog.decode(json).single()
        assertFalse(entry.signature.qualified);assertEquals(90000L,entry.signature.maxGapMs)
        assertThrows(IllegalArgumentException::class.java){catalog.decode(json.replace("\"qualified\": false","\"qualified\": true"))}
        assertThrows(IllegalArgumentException::class.java){catalog.decode(json.replace("\"schemaVersion\": 1","\"schemaVersion\": 2"))}
    }
    @Test fun prompt_preserves_procedural_gap_and_requires_all_policy_conditions() {
        val a=assess(slice("a",0,10000,event("f","flood",1000)),
            slice("b",30000,40000,event("d","drone-observation",31000))).single()
        val request=com.projectrevivesolutions.layertime.controller.signatures.SignatureBrief.request(a)
        assertTrue(request.messages[1].content.contains("\"observedGapMs\":30000"))
        assertTrue(request.messages[0].content.contains("all its required conditions"))
        val candidate=com.projectrevivesolutions.layertime.controller.signatures.SignatureBrief.request(a.copy(follow=null))
        assertTrue(candidate.messages[1].content.contains("\"observedGapMs\":null"))
    }
    @Test fun quiet_windows_do_not_request_assessment() {assertTrue(assess(slice("a",0,10000)).isEmpty())}
    @Test fun trigger_is_a_candidate_before_follow_on_arrives() {
        val a=assess(slice("a",0,10000,event("f","flood",1000))).single()
        assertEquals(State.CANDIDATE,a.state);assertNull(a.follow);assertFalse(a.qualified)
    }
    @Test fun sequence_spans_windows_without_inventing_continuous_coverage() {
        val a=assess(slice("a",0,10000,event("f","flood",1000)),
            slice("b",80000,90000,event("d","drone-observation",85000))).single()
        assertEquals(State.SEQUENCE_OBSERVED,a.state);assertEquals(listOf("a","b"),a.sliceIds)
        assertEquals(90000L,a.asOfMs);assertTrue(a.limitations.any {it.contains("incomplete")})
    }
    @Test fun reverse_order_and_outside_interval_do_not_match() {
        for(time in listOf(999L,1000L,91001L)) {
            val a=assess(slice("a",0,100000,event("f","flood",1000),event("d","drone-observation",time))).single()
            assertEquals(State.CANDIDATE,a.state)
        }
    }
    @Test fun exact_interval_boundary_is_inclusive() {
        assertEquals(State.SEQUENCE_OBSERVED,assess(slice("a",0,100000,
            event("f","flood",1000),event("d","drone-observation",91000))).single().state)
    }
    @Test fun sessions_and_unaligned_clocks_are_never_joined() {
        for(b in listOf(slice("b",10000,20000,event("d","drone-observation",15000),run="other"),
            slice("b",10000,20000,event("d","drone-observation",15000),clock="watch-unmapped"))) {
            assertEquals(State.CANDIDATE,assess(slice("a",0,10000,event("f","flood",1000)),b).single().state)
        }
    }
    @Test fun overlapping_windows_deduplicate_evidence_and_reject_conflicts() {
        val f=event("f","flood",1000)
        assertEquals(1,assess(slice("a",0,10000,f),slice("b",0,10000,f)).size)
        assertThrows(IllegalArgumentException::class.java) {
            assess(slice("a",0,10000,f),slice("b",0,10000,f.copy(atMs=2000)))
        }
    }
}
