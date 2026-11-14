package com.projectrevivesolutions.layertime.controller.state

import com.projectrevivesolutions.layertime.controller.link.Frame
import com.projectrevivesolutions.layertime.controller.link.LinkConstants
import com.projectrevivesolutions.layertime.controller.link.StatusSnapshot
import com.projectrevivesolutions.layertime.controller.model.BackgroundTest
import com.projectrevivesolutions.layertime.controller.model.ConnState
import com.projectrevivesolutions.layertime.controller.model.DeviceReport
import com.projectrevivesolutions.layertime.controller.model.EventRow
import com.projectrevivesolutions.layertime.controller.model.LogEntry
import com.projectrevivesolutions.layertime.controller.model.RssiSample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Single source of truth shared between the foreground service (which owns the
 * BLE link) and the Compose UI. All mutations append to the diagnostic log so
 * the exported bundle is a faithful, timestamped record.
 *
 * MutableStateFlow is safe to update from the GATT callback threads.
 */
object ControllerRepository {

    private val _conn = MutableStateFlow(ConnState.IDLE)
    val conn: StateFlow<ConnState> = _conn.asStateFlow()

    /** The reason passed with the latest state; the CONNECTION panel turns it into a sentence (ConnNotice). */
    private val _connDetail = MutableStateFlow<String?>(null)
    val connDetail: StateFlow<String?> = _connDetail.asStateFlow()

    private val _device = MutableStateFlow<String>("")
    val deviceLabel: StateFlow<String> = _device.asStateFlow()
    private val _hunterAddress=MutableStateFlow<String?>(null)
    val hunterAddress:StateFlow<String?> = _hunterAddress.asStateFlow()

    private val _status = MutableStateFlow<StatusSnapshot?>(null)
    val status: StateFlow<StatusSnapshot?> = _status.asStateFlow()

    private val _server = MutableStateFlow<Frame.HelloAck?>(null)
    val server: StateFlow<Frame.HelloAck?> = _server.asStateFlow()

    // The SetSensors mask the connected Hunter last answered Ok to; null until it has,
    // and again whenever the link is not up. MISSION compares it with its own list.
    private val _sensorsApplied = MutableStateFlow<Long?>(null)
    val sensorsApplied: StateFlow<Long?> = _sensorsApplied.asStateFlow()
    fun setSensorsApplied(mask: Long?) { _sensorsApplied.value = mask }
    // The evidence hold the Hunter last confirmed (SetEvidenceHold, 2026-11-04); null until it does.
    private val _evidenceHoldApplied = MutableStateFlow<Int?>(null)
    val evidenceHoldApplied: StateFlow<Int?> = _evidenceHoldApplied.asStateFlow()
    fun setEvidenceHoldApplied(sec: Int?) { _evidenceHoldApplied.value = sec }

    private val _events = MutableStateFlow<List<EventRow>>(emptyList())
    val events: StateFlow<List<EventRow>> = _events.asStateFlow()

    const val LOG_HISTORY_LIMIT = 2_000

    private val _log = MutableStateFlow<List<LogEntry>>(emptyList())
    val log: StateFlow<List<LogEntry>> = _log.asStateFlow()

    private val _background = MutableStateFlow<BackgroundTest?>(null)
    val background: StateFlow<BackgroundTest?> = _background.asStateFlow()

    @Volatile var deviceReport: DeviceReport? = null

    /** True after a link to a Hunter was lost, until it is back or the operator disconnects. */
    private val _reconnecting = MutableStateFlow(false)
    val reconnecting: StateFlow<Boolean> = _reconnecting.asStateFlow()
    fun setReconnecting(on: Boolean) { _reconnecting.value = on }

    /** True while connected but the Status heartbeat is more than 3 s late (LinkHealth). */
    private val _linkStale = MutableStateFlow(false)
    val linkStale: StateFlow<Boolean> = _linkStale.asStateFlow()
    fun setLinkStale(stale: Boolean) {
        if (_linkStale.value != stale) {
            _linkStale.value = stale
            log(LogEntry.Category.LINK, if (stale) "link stale: Status heartbeat late" else "link healthy")
        }
    }

    // Event mirror for the current session: the Hunter's records, shown one row per MAC.
    private var currentSession: Int = -1
    private val mirror = EventGrouper()
    private val mirrorLock = Any()
    private val _latestFieldAnalysis=MutableStateFlow<String?>(null)
    val latestFieldAnalysis=_latestFieldAnalysis.asStateFlow()
    private val _latestFieldSnapshot=MutableStateFlow<com.projectrevivesolutions.layertime.controller.ai.FieldShadowTasks.Snapshot?>(null)
    val latestFieldSnapshot=_latestFieldSnapshot.asStateFlow()

    private fun now() = System.currentTimeMillis()
    private fun fieldRecord(kind:String, values:Map<String,Any?>):ObservationJournal.Receipt? {
        return FieldObservationJournal.record(kind, linkedMapOf<String,Any?>(
            "hunter" to _device.value, "hunterAddress" to _hunterAddress.value, "sessionId" to currentSession).apply {putAll(values)})
    }
    fun onChangeGap(changeSeq:Long) {fieldRecord("change_gap",mapOf("changeSeq" to changeSeq))}


    fun log(category: LogEntry.Category, message: String) {
        _log.update { it.takeLast(LOG_HISTORY_LIMIT - 1) + LogEntry(now(), category, message) }
        // Also to logcat (tag Vixen), so a capture on the bench computer shows the app's own
        // view of the link next to Android's Bluetooth log (2026-11-06 diagnostics).
        runCatching { android.util.Log.i("Vixen", "[$category] $message") }
    }

    fun setConn(state: ConnState, detail: String? = null) {
        if (state != ConnState.CONNECTED) { _sensorsApplied.value = null; _evidenceHoldApplied.value = null }
        _connDetail.value = detail
        _conn.value = state
        fieldRecord("connection",mapOf("state" to state.name,"detail" to detail))
        log(LogEntry.Category.CONNECTION, "state=$state" + (detail?.let { " ($it)" } ?: ""))
    }

    fun setDeviceLabel(label: String, address:String? = null) {
        if(address!=null && !_hunterAddress.value.equals(address,ignoreCase=true)) {
            _hunterAddress.value=address
            _status.value=null;_server.value=null
            currentSession=-1
            _latestFieldAnalysis.value=null;_latestFieldSnapshot.value=null
            synchronized(mirrorLock) {mirror.clear()}
            publishEvents()
        }
        _device.value=label
    }

    fun onServerInfo(ack: Frame.HelloAck) {
        _server.value = ack
        // HELLO_ACK carries the session id, and the Hunter can send events before its
        // first STATUS. Take the session from here so no event is filed under session -1
        // (an unknown session broke detection-alert de-duplication across Hunter restarts).
        if (ack.sessionId != currentSession) {
            if (currentSession != -1) log(LogEntry.Category.LINK,
                "new session ${ack.sessionId} from HELLO_ACK (was $currentSession): clearing event mirror")
            currentSession = ack.sessionId
            _latestFieldAnalysis.value=null;_latestFieldSnapshot.value=null
            synchronized(mirrorLock) { mirror.clear() }
            publishEvents()
        }
        fieldRecord("hello",mapOf("capabilities" to ack.capabilities,"serverMajor" to ack.serverMajor,"serverMinor" to ack.serverMinor))
        val caps = LinkConstants.Capability.describe(ack.capabilities).joinToString(",")
        log(LogEntry.Category.LINK,
            "HELLO_ACK server ${ack.serverMajor}.${ack.serverMinor}, session=${ack.sessionId}, " +
                "caps=[$caps], maxFrame=${ack.maxFrame}, linkStatus=${LinkConstants.LinkStatus.name(ack.linkStatus)}")
    }

    fun onStatus(s: StatusSnapshot) {
        val prev = _status.value
        _status.value = s
        if (s.sessionId != currentSession) {
            // Session rule: a new session invalidates the whole mirror.
            if (currentSession != -1) log(LogEntry.Category.LINK,
                "new session ${s.sessionId} (was $currentSession): clearing event mirror")
            currentSession = s.sessionId
            _latestFieldAnalysis.value=null;_latestFieldSnapshot.value=null
            synchronized(mirrorLock) { mirror.clear() }
            publishEvents()
        }
        fieldRecord("status",mapOf("changeSeq" to s.changeSeq,"flags" to s.flags,"selected" to s.selected,
            "active" to s.active,"eventCount" to s.eventCount,"heartbeat" to s.heartbeat))
        // A record whose MAC never arrived is shown on its own after a few seconds; the
        // Status heartbeat is what lets that happen when no other frame comes.
        onMirrorChange(synchronized(mirrorLock) { mirror.flush(now()) })
        // Only log a status line when something meaningful changed (not heartbeat).
        if (prev == null || prev.changeSeq != s.changeSeq || prev.flags != s.flags ||
            prev.eventCount != s.eventCount || prev.active != s.active
        ) {
            log(LogEntry.Category.LINK,
                "STATUS seq=${s.changeSeq} flags=0x%02X events=${s.eventCount} active=${LinkConstants.ReconTarget.name(s.active)}"
                    .format(s.flags))
        }
    }

    /**
     * Upsert a Hunter record from an EVENT_SUMMARY frame; returns true if the record is new
     * (so its text fields still have to be fetched). The list shows one row per MAC.
     *
     * Logging, build 74: a repeat sighting no longer writes a line. A line is written when
     * a row first appears and when a row's confidence goes up. The row's count and signal
     * history keep the rest.
     */
    fun onEventSummary(e: Frame.EventSummary): Boolean {
        val inputReceipt=fieldRecord("event_summary",mapOf("eventId" to e.eventId,"detector" to e.detector,
            "confidence" to e.confidence,"sourceKind" to e.sourceKind,"band" to e.band,
            "channel" to e.channel,"rssi" to e.rssi,"count" to e.count,"countIsLowerBound" to (e.count==65535),
            "ageSeconds" to e.ageSeconds,"ageIsLowerBound" to (e.ageSeconds==65535),"flags" to e.flags))
        val t = now()
        val (new, change) = synchronized(mirrorLock) {
            val existing = mirror.record(e.eventId)
            val row = EventRow(
                sessionId = currentSession,
                producerAddress = _hunterAddress.value,
                eventId = e.eventId,
                detector = e.detector,
                detectorName = LinkConstants.ReconTarget.name(e.detector),
                confidence = e.confidence,
                sourceKind = e.sourceKind,
                band = e.band,
                channel = e.channel,
                rssi = e.rssi,
                count = e.count,
                ageSeconds = e.ageSeconds,
                hasSourceId = e.hasSourceId,
                hasDetail = e.hasDetail,
                sourceId = existing?.sourceId,
                detail = existing?.detail,
                firstSeenMillis = existing?.firstSeenMillis ?: t,
                lastUpdatedMillis = t,
                rssiHistory = ((existing?.rssiHistory ?: emptyList()) + RssiSample(t, e.rssi, System.nanoTime()/1_000_000L, e.eventId, e.count, e.ageSeconds)).takeLast(RssiSample.KEEP),
            )
            _latestFieldSnapshot.value=if(inputReceipt?.queued==true && row.producerAddress!=null && currentSession in 1..65535)
                com.projectrevivesolutions.layertime.controller.ai.FieldShadowTasks.Snapshot(inputReceipt.runId,inputReceipt.sequence,
                    row.producerAddress,currentSession,e.eventId,t,linkedMapOf(
                        "recordType" to "detector_event_summary","detector" to row.detectorName,
                        "radioDomain" to LinkConstants.SourceKind.name(row.sourceKind),"band" to LinkConstants.Band.name(row.band),
                        "channel" to row.channel,"rssiDbm" to row.rssi,"reportedSightings" to row.count,
                        "countIsLowerBound" to (row.count==65535),"reportedAgeSeconds" to row.ageSeconds,
                        "ageIsLowerBound" to (row.ageSeconds==65535),"detectorConfidence" to LinkConstants.Confidence.name(row.confidence),
                        "observedIdentifier" to row.sourceId,"reportedDetail" to row.detail)) else null
            val analysis=com.projectrevivesolutions.layertime.controller.events.EventTriage.classify(row)
            _latestFieldAnalysis.value="Hunter event #${e.eventId}: ${analysis.reason}"
            fieldRecord("event_analysis",mapOf("eventId" to e.eventId,"inputRunId" to inputReceipt?.runId,
                "inputSequence" to inputReceipt?.sequence,"inputQueued" to (inputReceipt?.queued==true),
                "engine" to "event_triage","modelUsed" to false,"verdict" to analysis.triage.name,"reason" to analysis.reason))
            (existing == null) to mirror.onRecord(row, t)
        }
        onMirrorChange(change)
        // Count toward an active background test.
        _background.update { bt -> if (bt?.running == true) bt.copy(eventsReceivedDuring = bt.eventsReceivedDuring + 1) else bt }
        return new
    }

    fun onEventText(eventId: Long, field: Int, text: String) {
        fieldRecord("event_text",mapOf("eventId" to eventId,"field" to field,"text" to text))
        onMirrorChange(synchronized(mirrorLock) { mirror.onText(eventId, field, text, now()) })
        val current=_latestFieldSnapshot.value
        if(current!=null && current.sessionId==currentSession && current.eventId==eventId && current.hunterAddress==_hunterAddress.value) {
            val key=when(field){LinkConstants.TextField.SOURCE_ID->"observedIdentifier";LinkConstants.TextField.DETAIL->"reportedDetail";else->null}
            if(key!=null)_latestFieldSnapshot.value=current.withFacts(current.facts+mapOf(key to text))
        }
    }

    private fun onMirrorChange(change: EventGrouper.Change) {
        for (r in change.newRows) log(LogEntry.Category.EVENT, "NEW " + describeRow(r))
        for ((was, r) in change.raised) log(LogEntry.Category.EVENT,
            "RAISED ${LinkConstants.Confidence.name(was)} -> ${LinkConstants.Confidence.name(r.confidence)}: " + describeRow(r))
        if (change.touched) publishEvents()
    }

    private fun describeRow(r: EventRow): String =
        "#${r.eventId} ${r.detectorName} ${r.sourceId ?: "(no id)"} ${LinkConstants.SourceKind.name(r.sourceKind)} " +
            "${LinkConstants.Band.name(r.band)} ch${r.channel} ${r.rssi}dBm x${r.count} " +
            "conf=${LinkConstants.Confidence.name(r.confidence)}" + (r.detail?.let { " \"$it\"" } ?: "") +
            (if (r.mergedEventIds.size > 1) " (${r.mergedEventIds.size} Hunter records)" else "")

    private fun publishEvents() {
        _events.value = synchronized(mirrorLock) { mirror.rows() }
    }

    /** Rows on screen: one per MAC address. */
    fun eventCount(): Int = synchronized(mirrorLock) { mirror.rowCount() }

    // ---- background / screen-off reception test ----

    fun startBackgroundTest(note: String) {
        _background.value = BackgroundTest(
            running = true, startedMillis = now(), endedMillis = null,
            eventsAtStart = eventCount(), eventsReceivedDuring = 0, disconnectsDuring = 0, note = note
        )
        log(LogEntry.Category.BACKGROUND, "background reception test STARTED ($note)")
    }

    fun noteBackgroundDisconnect() {
        _background.update { bt -> if (bt?.running == true) bt.copy(disconnectsDuring = bt.disconnectsDuring + 1) else bt }
    }

    fun stopBackgroundTest() {
        _background.update { bt ->
            if (bt?.running == true) {
                val done = bt.copy(running = false, endedMillis = now())
                log(LogEntry.Category.BACKGROUND,
                    "background reception test ENDED: received ${done.eventsReceivedDuring} event(s), " +
                        "${done.disconnectsDuring} disconnect(s) over ${(done.endedMillis!! - done.startedMillis) / 1000}s")
                done
            } else bt
        }
    }

    /** The Hunter cleared its detections (ReconClearEvents answered OK): the same session, an empty list. */
    fun clearEventList() {
        fieldRecord("events_cleared",emptyMap())
        _latestFieldAnalysis.value=null;_latestFieldSnapshot.value=null
        synchronized(mirrorLock) { mirror.clear() }
        _events.value = emptyList()
    }

    fun reset() {
        synchronized(mirrorLock) { mirror.clear() }; currentSession = -1; _hunterAddress.value=null; _latestFieldAnalysis.value=null;_latestFieldSnapshot.value=null
        _events.value = emptyList(); _status.value = null; _server.value = null
        _background.value = null
    }
}
