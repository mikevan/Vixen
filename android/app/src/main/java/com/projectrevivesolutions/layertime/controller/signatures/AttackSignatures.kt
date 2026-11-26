package com.projectrevivesolutions.layertime.controller.signatures

/** Offline candidate correlation. A match is never proof of an attack or cause.
 * Times must already share an explicitly aligned observation clock, never receipt
 * times silently substituted for RF timestamps. Raw captures remain external.
 */
object AttackSignatures {
    data class Signature(val id:String, val revision:Int, val firstKind:String,
        val followKind:String, val maxGapMs:Long, val qualified:Boolean=false) {
        init {
            require(id.matches(Regex("[a-z][a-z0-9-]{0,79}")) && revision>0)
            require(firstKind.isNotBlank() && followKind.isNotBlank() && maxGapMs>0)
        }
    }
    data class Observation(val id:String,val kind:String,val atMs:Long,
        val producer:String,val evidenceRef:String) {
        init { require(listOf(id,kind,producer,evidenceRef).all {it.isNotBlank()} && atMs>=0) }
    }
    data class Slice(val id:String,val runId:String,val clockDomain:String,
        val startMs:Long,val endMs:Long,val observations:List<Observation>,val limitations:List<String>) {
        init {
            require(listOf(id,runId,clockDomain).all {it.isNotBlank()})
            require(startMs>=0 && endMs>=startMs)
            require(observations.all {it.atMs in startMs..endMs})
            require(observations.map {it.id}.distinct().size==observations.size)
        }
    }
    enum class State { CANDIDATE, SEQUENCE_OBSERVED }
    data class Assessment(val signatureId:String,val revision:Int,val qualified:Boolean,
        val runId:String,val clockDomain:String,val first:Observation,val follow:Observation?,
        val state:State,val sliceIds:List<String>,val limitations:List<String>,val asOfMs:Long)

    /** Only interesting procedural observations enter here. Quiet slices do not
     * invoke AI. Missing follow-on evidence stays unresolved, including after the
     * configured interval. Multiple 10-second slices can support one signature.
     */
    fun assess(signature:Signature,slices:List<Slice>):List<Assessment> {
        require(slices.map {it.id}.distinct().size==slices.size)
        val output=mutableListOf<Assessment>()
        for((scope,windows) in slices.groupBy {it.runId to it.clockDomain}) {
            val byId=windows.flatMap {it.observations}.groupBy {it.id}
            require(byId.values.all {it.distinct().size==1}) {"Conflicting observation identity"}
            val observations=byId.values.map {it.first()}.sortedWith(compareBy({it.atMs},{it.id}))
            for(first in observations.filter {it.kind==signature.firstKind}) {
                val follow=observations.firstOrNull {it.id!=first.id && it.kind==signature.followKind &&
                    it.atMs>first.atMs && it.atMs-first.atMs<=signature.maxGapMs}
                // Include only windows intersecting this candidate's relevant interval.
                val horizon=if(Long.MAX_VALUE-first.atMs<signature.maxGapMs) Long.MAX_VALUE
                    else first.atMs+signature.maxGapMs
                val relevant=windows.filter {it.endMs>=first.atMs && it.startMs<=(follow?.atMs ?: horizon)}
                output+=Assessment(signature.id,signature.revision,signature.qualified,scope.first,scope.second,
                    first,follow,if(follow==null) State.CANDIDATE else State.SEQUENCE_OBSERVED,
                    relevant.map {it.id}.sorted(),(relevant.flatMap {it.limitations}+
                        listOf("A temporal association does not establish cause, hostile intent or physical identity.",
                            "Missing observations do not establish absence; capture coverage may be incomplete.")+
                        if(signature.qualified) emptyList() else listOf("Draft signature; not field-qualified.")).distinct(),
                    relevant.maxOf {it.endMs})
            }
        }
        return output.sortedWith(compareBy({it.runId},{it.clockDomain},{it.first.atMs},{it.first.id}))
    }
}
