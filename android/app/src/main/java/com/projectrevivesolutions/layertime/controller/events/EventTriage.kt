package com.projectrevivesolutions.layertime.controller.events

import com.projectrevivesolutions.layertime.controller.model.EventRow

/** Evidence sorting, not a determination of maliciousness or physical tracking.
 * TRACK: a tracker-family detector with fresh, increasing reports received over
 * the configured observation span. KEEP: medium/high confidence without that
 * span. NOISE: low confidence without an established span, not proof of benignness.
 * Counts alone, replayed summaries, mixed Hunter IDs and wall-clock changes must
 * not establish persistence. Missing timing/provenance remains unresolved.
 */
object EventTriage {
    const val CONF_LOW = 0
    const val CONF_MED = 1
    const val CONF_HIGH = 2

    // ReconTarget numbers for tracker-family detectors (contracts/vectors/enums.json):
    // Flock(8), AirTag(10), Meta(12), Axon(13), Tile(14), SmartTag(15), GoogleTag(16).
    val TRACKER_DETECTORS = setOf(8, 10, 12, 13, 14, 15, 16)

    // Minimum span between fresh increasing receipts; not verified RF dwell.
    const val DWELL_MILLIS = 120_000L

    enum class Triage { TRACK, KEEP, NOISE }

    data class Verdict(val triage: Triage, val reason: String)

    // A receipt older than this is not a fresh sample. This is an observation
    // freshness limit, not a threat definition. Wire age is quantized seconds.
    const val MAX_FRESH_AGE_SECONDS = 3

    /** Span of fresh, increasing reports for one Hunter record; never RF dwell proof.
     * Old samples without provenance cannot establish persistence. Grouped MAC
     * rows may contain several detector/event histories; do not join their counts.
     */
    fun dwellSpanMillis(e: EventRow): Long {
        val groups=e.rssiHistory.filter {it.receivedMonotonicMs!=null && it.hunterEventId!=null &&
            it.sightingCount!=null && it.ageSeconds!=null && it.ageSeconds in 0..MAX_FRESH_AGE_SECONDS}
            .groupBy {it.hunterEventId}
        return groups.values.maxOfOrNull { samples ->
            // Keep the first receipt of each count: replaying a new count later
            // must not move its endpoint forward (including saturated 65535).
            val increases=samples.distinctBy {it.sightingCount}
            var span=0L
            for(i in increases.indices) for(j in i+1 until increases.size) {
                val first=increases[i];val last=increases[j]
                if(last.sightingCount!! > first.sightingCount!! &&
                    last.receivedMonotonicMs!! >= first.receivedMonotonicMs!!)
                    span=maxOf(span,last.receivedMonotonicMs-first.receivedMonotonicMs)
            }
            span
        } ?: 0L
    }

    fun dwells(e: EventRow): Boolean = dwellSpanMillis(e) >= DWELL_MILLIS

    fun classify(e: EventRow): Verdict {
        if (e.detector in TRACKER_DETECTORS && dwells(e)) {
            return Verdict(Triage.TRACK, "fresh reports received over ${dwellSpanMillis(e) / 1000}s; reported count ${e.count}. This does not establish physical tracking.")
        }
        if (e.confidence <= CONF_LOW) {
            return Verdict(Triage.NOISE, "low confidence; no fresh repeat-report span established")
        }
        return Verdict(Triage.KEEP, "confidence ${confName(e.confidence)}")
    }

    fun partition(rows: List<EventRow>): Map<Triage, List<EventRow>> =
        rows.groupBy { classify(it).triage }

    fun counts(rows: List<EventRow>): Map<Triage, Int> = mapOf(
        Triage.TRACK to rows.count { classify(it).triage == Triage.TRACK },
        Triage.KEEP to rows.count { classify(it).triage == Triage.KEEP },
        Triage.NOISE to rows.count { classify(it).triage == Triage.NOISE },
    )

    private fun confName(v: Int) = when (v) { CONF_LOW -> "LOW"; CONF_MED -> "MED"; CONF_HIGH -> "HIGH"; else -> "?($v)" }
}
