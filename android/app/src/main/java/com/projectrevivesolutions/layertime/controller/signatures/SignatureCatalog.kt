package com.projectrevivesolutions.layertime.controller.signatures
import com.projectrevivesolutions.layertime.controller.flow.FlowJson

/** Draft-only catalog intake. Automatic warning activation requires a future
 * separately reviewed qualification policy; a JSON flag cannot qualify a model.
 */
object SignatureCatalog {
    data class Entry(val signature:AttackSignatures.Signature,val requiredEvidence:List<String>,
        val lookalikes:List<String>,val qualificationRequired:List<String>,val limits:List<String>)
    fun decode(json:String):List<Entry> {
        require(json.length<=128000)
        val root=FlowJson.parse(json) as? Map<*,*> ?: error("Invalid signature catalog")
        require(root["schemaVersion"]==1L && root["libraryRevision"]==1L)
        val rows=root["signatures"] as? List<*> ?: error("Missing signatures")
        require(rows.size in 1..64)
        val entries=rows.map {value->
            val row=value as? Map<*,*> ?: error("Invalid signature")
            fun text(key:String)=(row[key] as? String)?.takeIf {it.isNotBlank()} ?: error("Missing $key")
            fun number(key:String)=(row[key] as? Long)?.takeIf {it>0} ?: error("Invalid $key")
            fun strings(key:String):List<String> {
                val values=row[key] as? List<*> ?: error("Missing $key")
                require(values.isNotEmpty() && values.size<=64)
                return values.map {it as? String ?: error("Invalid $key")}.also {require(it.all {s->s.isNotBlank()})}
            }
            require(row["qualified"]==false && row["automaticWarningEnabled"]==false && text("status")=="draft-hypothesis")
            val revision=number("revision");require(revision<=Int.MAX_VALUE)
            require(number("maxGapMs")<=86400000 && number("captureWindowMs")==10000L)
            text("provenance")
            Entry(AttackSignatures.Signature(text("id"),revision.toInt(),text("firstKind"),text("followKind"),number("maxGapMs")),
                strings("requiredEvidence"),strings("lookalikes"),strings("qualificationRequired"),strings("limits"))
        }
        require(entries.map {it.signature.id}.distinct().size==entries.size)
        return entries
    }
}
