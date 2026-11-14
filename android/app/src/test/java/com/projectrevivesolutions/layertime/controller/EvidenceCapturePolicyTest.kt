package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.signatures.EvidenceCapturePolicy
import org.junit.Assert.*
import org.junit.Test
class EvidenceCapturePolicyTest {
    @Test fun repeated_summary_does_not_retrieve_the_same_window_forever() {
        val p=EvidenceCapturePolicy()
        assertTrue(p.shouldCapture("a",1,7,1,1,0,0))
        assertFalse(p.shouldCapture("a",1,7,1,1,0,10000))
        assertTrue(p.shouldCapture("a",1,7,2,1,0,11000))
        assertFalse(p.shouldCapture("a",1,7,1,1,0,22000))
        assertFalse(p.shouldCapture("a",1,7,2,1,0,33000))
    }
    @Test fun fresh_confidence_increase_is_interesting_but_stale_reports_are_not() {
        val p=EvidenceCapturePolicy()
        assertFalse(p.shouldCapture("a",1,7,1,0,0,0))
        assertFalse(p.shouldCapture("a",1,7,1,1,4,1))
        assertTrue(p.shouldCapture("a",1,7,1,1,0,2))
    }
    @Test fun independent_producers_and_sessions_do_not_suppress_one_another() {
        val p=EvidenceCapturePolicy()
        assertTrue(p.shouldCapture("a",1,7,1,1,0,0))
        assertTrue(p.shouldCapture("b",1,7,1,1,0,0))
        assertTrue(p.shouldCapture("a",2,7,1,1,0,0))
    }
    @Test fun rapid_updates_are_bounded_and_saturated_counts_do_not_prove_new_activity() {
        val p=EvidenceCapturePolicy()
        assertTrue(p.shouldCapture("a",1,7,65534,1,0,0))
        assertFalse(p.shouldCapture("a",1,7,65535,1,0,1))
        assertFalse(p.shouldCapture("a",1,7,65535,1,0,10001))
    }
}
