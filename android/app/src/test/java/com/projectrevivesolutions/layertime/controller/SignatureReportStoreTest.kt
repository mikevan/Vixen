package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.signatures.SignatureReportStore
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files
import java.io.File
class SignatureReportStoreTest {
    @Test fun archive_is_idempotent_and_preserves_original_report() {
        val root=Files.createTempDirectory("reports").toFile()
        try {
            val id=SignatureReportStore.idForKey("producer/session/events")
            val file=File(root,"signature-assessments/$id.json").apply {parentFile!!.mkdirs()}
            val json="""{"status":"AI analysis complete","finding":"Recorded finding","completedEpochMs":1234,"source":[{"sessionId":42}]}"""
            file.writeText(json);val modified=file.lastModified();val store=SignatureReportStore(root)
            assertEquals(1234L,store.list().single().completedEpochMs)
            assertEquals("42",store.list().single().sessions)
            store.archive(id,true);store.archive(id,true)
            assertTrue(SignatureReportStore(root).list().single().archived)
            assertEquals(json,file.readText());assertEquals(modified,file.lastModified())
            store.archive(id,false);store.archive(id,false)
            assertFalse(store.list().single().archived);assertEquals(json,store.list().single().json)
        } finally {root.deleteRecursively()}
    }
    @Test fun unknown_creation_times_remain_unknown_and_corrupt_records_do_not_hide_history() {
        val root=Files.createTempDirectory("reports").toFile()
        try {
            val dir=File(root,"signature-assessments").apply {mkdirs()}
            File(dir,"${"a".repeat(64)}.json").writeText("""{"status":"pending"}""")
            File(dir,"${"b".repeat(64)}.json").writeText("invalid")
            val report=SignatureReportStore(root).list().single()
            assertNull(report.completedEpochMs);assertNull(report.finding)
        }finally {root.deleteRecursively()}
    }
}
