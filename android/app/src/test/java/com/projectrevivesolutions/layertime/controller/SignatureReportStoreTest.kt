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
    @Test fun archived_current_never_falls_back_to_older_report_after_restart() {
        val root=Files.createTempDirectory("reports").toFile()
        try {
            val store=SignatureReportStore(root)
            val dir=File(root,"signature-assessments").apply {mkdirs()}
            val older=SignatureReportStore.idForKey("older");val newest=SignatureReportStore.idForKey("newest")
            File(dir,"$older.json").writeText("""{"completedEpochMs":100,"finding":"old"}""")
            File(dir,"$newest.json").writeText("""{"completedEpochMs":200,"finding":"new"}""")
            assertEquals(newest,store.current(store.list(),null)!!.id)
            store.archive(newest,true)
            assertNull(store.current(store.list(),"newest"));assertNull(SignatureReportStore(root).current(store.list(),null))
            assertEquals(2,store.list().size)
            store.archive(newest,false);assertEquals(newest,store.current(store.list(),null)!!.id)
        } finally {root.deleteRecursively()}
    }

}
