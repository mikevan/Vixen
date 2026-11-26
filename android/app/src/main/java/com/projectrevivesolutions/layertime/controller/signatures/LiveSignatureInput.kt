package com.projectrevivesolutions.layertime.controller.signatures

import com.projectrevivesolutions.layertime.controller.model.EventRow
import com.projectrevivesolutions.layertime.controller.flow.FlowJson

/** Selection and arithmetic only. The model selects the interpretation. Never
 * combine producer sessions or substitute receipt times for source uptime. */
object LiveSignatureInput {
    data class Input(val key:String,val json:String,val records:List<EventRow>,val gapMs:Long?)
    private fun delta(a:Long,b:Long):Long {val d=(b-a) and 0xffffffffL;return if(d>=0x80000000L)d-0x100000000L else d}
    fun prepare(rows:List<EventRow>,windowMs:Long=90000):Input? {
        require(windowMs in 1..3600000)
        val relevant=rows.filter {it.detector in setOf(5,18,19) && it.producerAddress!=null && it.sessionId in 1..65535}
        val scopes=relevant.groupBy {it.producerAddress to it.sessionId}
        // One source/session per assessment. No borrowed corroboration.
        for((scope,group) in scopes) {
            // Rank complete same-network pairs, not disconnects alone. An unmatched
            // newer network must not hide another network's complete evidence.
            // Timing remains evidence for the model: do not filter negative order
            // or outside-window cases merely to manufacture a positive candidate.
            val pair=group.asSequence()
                .filter {it.detector==5 && !it.sourceId.isNullOrBlank()}
                .sortedWith(compareByDescending<EventRow> {it.firstSeenMillis}.thenByDescending {it.eventId})
                .mapNotNull {disconnect ->
                    group.filter {it.detector==18 && it.sourceId.equals(disconnect.sourceId,true)}
                        .maxWithOrNull(compareBy<EventRow> {it.firstSeenMillis}.thenBy {it.eventId})
                        ?.let {disconnect to it}
                }.firstOrNull() ?: continue
            val (deauth,exchange)=pair
            val remote=group.filter {it.detector==19 && validRemoteId(it.detail)}.maxByOrNull {it.firstSeenMillis} ?: continue
            val selected=listOf(deauth,exchange,remote)
            val clock=selected.all {it.sourceFirstUptimeMs!=null}
            val anchor=deauth.sourceFirstUptimeMs ?: 0
            val offsets=selected.map {it.sourceFirstUptimeMs?.let {t->delta(anchor,t)}}
            val pairOrder=if(clock)offsets[1]!!>0 else null
            val remoteAfter=if(clock)offsets[2]!!>maxOf(0,offsets[1]!!) else null
            val gap=offsets[2]
            val events=selected.mapIndexed {i,row -> sortedMapOf<String,Any?>("id" to row.eventId.toString(),
                "kind" to listOf("deauth","eapol","remote_id")[i],
                "observedMs" to offsets[i]?.let {it+0x100000000L},
                "network" to if(i==2)null else row.sourceId?.lowercase())}.sortedBy {(it["observedMs"] as? Long) ?: Long.MAX_VALUE}
            val relations=sortedMapOf<String,Any?>("clockAligned" to clock,"sameNetworkDisconnectExchange" to true,
                "disconnectBeforeExchange" to pairOrder,"remoteIdAfterExchange" to remoteAfter,
                "disconnectToRemoteIdMs" to gap,"configuredWindowMs" to windowMs,
                "withinWindow" to gap?.let {it>0 && it<=windowMs})
            val json=FlowJson.write(sortedMapOf("events" to events,"relations" to relations,"remoteIdAuthenticityVerified" to false))
            val key="${scope.first}/${scope.second}/"+selected.joinToString("/"){"${it.eventId}:${it.sourceFirstUptimeMs}"}
            return Input(key,json,selected,gap)
        }
        return null
    }
    fun validRemoteId(detail:String?):Boolean {
        if(detail==null || !detail.matches(Regex("RID1:[0-9A-Fa-f]{54}")))return false
        val bytes=detail.substring(5).chunked(2).map {it.toInt(16)}
        return bytes[0]==13 && bytes[2] shr 4==0 && (bytes[2] and 15)<=2 &&
            bytes[3] shr 4 in 1..4 && bytes.subList(4,24).any {it!=0}
    }
}
