package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.signatures.*
import org.junit.Assert.*
import org.junit.Test
class EvacuationPolicyTest {
    private val outage=EvacuationPolicy.Outage(120,0,80,0,true,true,0)
    private fun assessment(follow:Long?=50000):AttackSignatures.Assessment {
        val s=AttackSignatures.Signature("evacuation-event",1,"wifi-ble-outage","drone-observation",90000)
        val first=AttackSignatures.Observation("1","wifi-ble-outage",20000,"Hunter","slice-1")
        val windows=mutableListOf(AttackSignatures.Slice("1","run","clock",10000,30000,listOf(first),emptyList()))
        if(follow!=null)windows.add(AttackSignatures.Slice("2","run","clock",follow,follow,
            listOf(AttackSignatures.Observation("2","drone-observation",follow,"observer","slice-2")),emptyList()))
        return AttackSignatures.assess(s,windows).single()
    }
    @Test fun thirty_seconds_is_inside_ninety_second_rule() {
        val d=EvacuationPolicy.evaluate(assessment(),outage)
        assertTrue(d.matched);assertEquals(30000L,d.gapMs);assertEquals("EVACUATION EVENT",d.headline)
        assertTrue(d.finding.contains("30 seconds later, within the 90 seconds policy window"))
    }
    @Test fun missing_follow_on_cannot_complete_rule() {assertFalse(EvacuationPolicy.evaluate(assessment(null),outage).matched)}
    @Test fun receiver_gaps_or_missing_baseline_are_not_verified_outages() {
        for(o in listOf(outage.copy(receiverHealthy=false),outage.copy(selectedCoverageVerified=false),
            outage.copy(droppedRecords=1),outage.copy(wifiBaseline=0),outage.copy(bleObserved=1)))
            assertFalse(EvacuationPolicy.evaluate(assessment(),o).matched)
    }
    @Test fun outside_window_cannot_match() {assertFalse(EvacuationPolicy.evaluate(assessment(110001),outage).matched)}
}
