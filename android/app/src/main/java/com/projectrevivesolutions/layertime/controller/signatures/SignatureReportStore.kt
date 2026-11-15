package com.projectrevivesolutions.layertime.controller.signatures

import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import java.io.File
import java.security.MessageDigest

/** Presentation metadata is separate from immutable inference inputs and results. */
class SignatureReportStore(private val root: File) {
    data class Report(val id:String,val completedEpochMs:Long?,val status:String,val finding:String?,val sessions:String,val archived:Boolean,val json:String)
    fun list():List<Report> = File(root,"signature-assessments").listFiles().orEmpty()
        .filter { it.extension=="json" && it.nameWithoutExtension.matches(Regex("[0-9a-f]{64}")) }
        .mapNotNull { file -> runCatching {
            val json=file.readText(); val value=FlowJson.parse(json) as Map<*,*>
            val time=(value["completedEpochMs"] as? Number)?.toLong()
            val sessions=(value["source"] as? List<*>)?.mapNotNull { (it as? Map<*,*>)?.get("sessionId") }?.distinct()?.joinToString(", ") ?: "Unknown"
            Report(file.nameWithoutExtension,time,value["status"] as? String ?: "Unknown status",value["finding"] as? String,sessions,marker(file.nameWithoutExtension).exists(),json)
        }.getOrNull() }.sortedByDescending { it.completedEpochMs ?: 0L }
    fun archive(id:String, archived:Boolean) {
        require(id.matches(Regex("[0-9a-f]{64}")))
        require(File(root,"signature-assessments/$id.json").isFile)
        val file=marker(id)
        if(archived) { check(file.parentFile!!.mkdirs() || file.parentFile!!.isDirectory); if(!file.exists())check(file.createNewFile()) }
        else if(file.exists())check(file.delete())
    }
    private fun marker(id:String)=File(root,"signature-report-archive/$id")
    companion object {
        fun idForKey(key:String)=MessageDigest.getInstance("SHA-256").digest(key.toByteArray()).joinToString(""){"%02x".format(it)}
    }
}
