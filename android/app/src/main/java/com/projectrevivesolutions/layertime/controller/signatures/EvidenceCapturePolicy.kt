package com.projectrevivesolutions.layertime.controller.signatures

/** Procedural packet preservation policy; not an attack or flood classifier. */
class EvidenceCapturePolicy {
    private data class Seen(val count:Int,val confidence:Int,val requestedAt:Long?)
    private val seen=linkedMapOf<String,Seen>()
    fun shouldCapture(peer:String,session:Int,event:Long,count:Int,confidence:Int,ageSeconds:Int,nowMs:Long):Boolean {
        if(peer.isBlank() || session !in 1..65535 || event !in 0..0xffffffffL || count !in 1..65535 ||
            confidence !in 0..2 || ageSeconds !in 0..3 || nowMs<0)return false
        val key="$peer/$session/$event"
        val previous=seen[key]
        val changed=previous==null || count>previous.count || confidence>previous.confidence
        val due=previous?.requestedAt?.let {nowMs>=it && nowMs-it>=10000} ?: true
        val capture=confidence>=1 && changed && due
        seen[key]=Seen(maxOf(count,previous?.count ?: 0),maxOf(confidence,previous?.confidence ?: 0),if(capture)nowMs else previous?.requestedAt)
        if(seen.size>256)seen.remove(seen.keys.first())
        return capture
    }
}
