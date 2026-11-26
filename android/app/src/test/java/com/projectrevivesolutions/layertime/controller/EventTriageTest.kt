package com.projectrevivesolutions.layertime.controller

import com.projectrevivesolutions.layertime.controller.events.EventTriage
import com.projectrevivesolutions.layertime.controller.model.EventRow
import com.projectrevivesolutions.layertime.controller.model.RssiSample
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EventTriageTest {

    private fun row(
        detector: Int = 10, confidence: Int = 0, count: Int = 1,
        history: List<RssiSample> = emptyList(), sourceId: String? = "AA:BB:CC:DD:EE:FF",
    ) = EventRow(
        sessionId = 1, eventId = 1, detector = detector, detectorName = "AIRTAG",
        confidence = confidence, sourceKind = 2, band = 1, channel = 6, rssi = -70,
        count = count, ageSeconds = 0, hasSourceId = sourceId != null, hasDetail = false, sourceId = sourceId,
        firstSeenMillis = 0L, lastUpdatedMillis = 0L, rssiHistory = history,
        mergedEventIds = listOf(1),
    )

    @Test fun lowConfidenceNonDwellIsNoise() {
        assertEquals(EventTriage.Triage.NOISE, EventTriage.classify(row(confidence = 0, count = 1)).triage)
    }

    @Test fun medConfidenceIsKept() {
        assertEquals(EventTriage.Triage.KEEP, EventTriage.classify(row(confidence = 1)).triage)
    }

    @Test fun highConfidenceAwayTagIsKept() {
        // Away-from-owner AirTag is graded HIGH by the Hunter, so it must survive triage.
        assertEquals(EventTriage.Triage.KEEP, EventTriage.classify(row(confidence = 2)).triage)
    }

    @Test fun trackerCountAloneIsNotTracked() {
        // Field test 2026-11-02: stationary nearby Apple devices reached counts of 40 with
        // zero time span. Count is sighting volume, not persistence, so it must stay NOISE.
        val v = EventTriage.classify(row(detector = 10, confidence = 0, count = 40))
        assertEquals(EventTriage.Triage.NOISE, v.triage)
    }

    @Test fun trackerThatLingersIsTracked() {
        val hist = listOf(RssiSample(0L, -70, 0L, 1L, 1, 0), RssiSample(EventTriage.DWELL_MILLIS, -65, EventTriage.DWELL_MILLIS, 1L, 2, 0))
        assertEquals(EventTriage.Triage.TRACK, EventTriage.classify(row(detector = 14, confidence = 0, count = 1, history = hist)).triage)
    }

    @Test fun nonTrackerLowStaysNoiseEvenIfRepeated() {
        // Deauth(5) is not a tracker; repetition alone does not make it TRACK, and LOW is noise.
        assertEquals(EventTriage.Triage.NOISE, EventTriage.classify(row(detector = 5, confidence = 0, count = 99)).triage)
    }

    @Test fun dwellSpanIsLastMinusFirst() {
        val hist = listOf(RssiSample(1000L, -70, 1000L, 1L, 1, 0), RssiSample(6000L, -60, 6000L, 1L, 2, 0))
        assertEquals(5000L, EventTriage.dwellSpanMillis(row(history = hist)))
    }

    @Test fun dwellSpanZeroForSingleReading() {
        assertEquals(0L, EventTriage.dwellSpanMillis(row(history = listOf(RssiSample(1000L, -70)))))
    }

    @Test fun redelivered_or_saturated_counts_do_not_establish_persistence() {
        for(count in listOf(40,65535)) {
            val history=listOf(RssiSample(0,-70,0,1,count,0),RssiSample(60000,-70,60000,1,count,0))
            assertEquals(0L,EventTriage.dwellSpanMillis(row(history=history)))
        }
    }
    @Test fun an_old_record_or_different_event_must_not_extend_the_span() {
        val first=RssiSample(0,-70,0,1,1,0)
        val old=RssiSample(60000,-70,60000,1,2,60)
        val other=RssiSample(60000,-70,60000,2,2,0)
        assertEquals(0L,EventTriage.dwellSpanMillis(row(history=listOf(first,old))))
        assertEquals(0L,EventTriage.dwellSpanMillis(row(history=listOf(first,other))))
    }
    @Test fun a_wall_clock_jump_does_not_manufacture_a_report_span() {
        val history=listOf(RssiSample(0,-70,1000,1,1,0),RssiSample(3600000,-70,2000,1,2,0))
        assertEquals(1000L,EventTriage.dwellSpanMillis(row(history=history)))
        assertFalse(EventTriage.dwells(row(history=history)))
    }
    @Test fun legacy_signal_history_cannot_prove_persistence() {
        assertEquals(0L,EventTriage.dwellSpanMillis(row(history=listOf(RssiSample(0,-70),RssiSample(60000,-70)))))
    }

    @Test fun countsSumToTotal() {
        val rows = listOf(
            row(confidence = 0, count = 1),
            row(confidence = 2),
            row(detector = 10, confidence = 0, count = 1, history = listOf(RssiSample(0L, -70, 0L, 1L, 1, 0), RssiSample(EventTriage.DWELL_MILLIS, -65, EventTriage.DWELL_MILLIS, 1L, 2, 0))),
        )
        val c = EventTriage.counts(rows)
        assertEquals(1, c[EventTriage.Triage.NOISE])
        assertEquals(1, c[EventTriage.Triage.KEEP])
        assertEquals(1, c[EventTriage.Triage.TRACK])
        assertTrue(EventTriage.TRACKER_DETECTORS.contains(10))
        assertFalse(EventTriage.TRACKER_DETECTORS.contains(5))
    }
    @Test fun replaying_a_once_increased_count_does_not_extend_its_original_endpoint() {
        val history=listOf(RssiSample(0,-70,0,1,65534,0),RssiSample(1000,-70,1000,1,65535,0),
            RssiSample(600000,-70,600000,1,65535,0))
        assertEquals(1000L,EventTriage.dwellSpanMillis(row(history=history)))
        assertFalse(EventTriage.dwells(row(history=history)))
    }
}
