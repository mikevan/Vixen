package com.projectrevivesolutions.layertime.controller.link

/** Provenance of a pulled window. Receipt time is not the Hunter's RF capture clock. */
data class HunterEvidenceOrigin(val hunterAddress:String,val sessionId:Int,val eventId:Long,
    val receivedEpochMs:Long,val payloadSha256:String,val collecting:Boolean) {
    init {require(hunterAddress.matches(Regex("[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}")))
        require(sessionId in 1..65535 && eventId in 0..0xffffffffL)
        require(payloadSha256.matches(Regex("[0-9a-f]{64}")))}
    fun matches(session:Int,event:Long,peer:String?) = peer!=null &&
        hunterAddress.equals(peer,ignoreCase=true) && sessionId==session && eventId==event
}
