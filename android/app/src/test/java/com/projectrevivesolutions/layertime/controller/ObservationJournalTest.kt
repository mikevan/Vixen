package com.projectrevivesolutions.layertime.controller

import com.projectrevivesolutions.layertime.controller.state.ObservationJournal
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import org.junit.Assert.*
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File

class ObservationJournalTest {
    @get:Rule val temp=TemporaryFolder()
    private fun read(file:File)=file.readLines().map {FlowJson.parse(it) as Map<*,*>}
    @Test fun individual_records_and_untrusted_text_survive_without_ui_merging() {
        ObservationJournal(temp.newFolder()).use {j ->
            val text="name, \"quoted\"\nnot an instruction"
            j.record("event_summary",1000,100,mapOf("sessionId" to 2,"eventId" to 7,"detector" to 1,"sourceId" to "AA"))
            j.record("event_summary",1001,101,mapOf("sessionId" to 2,"eventId" to 8,"detector" to 2,"sourceId" to "AA"))
            j.record("event_text",1002,102,mapOf("eventId" to 7,"text" to text))
            assertTrue(j.awaitIdle());val copy=File(temp.root,"copy.jsonl");val h=j.snapshot(copy)
            val rows=read(copy);assertEquals(3,rows.size);assertEquals(3L,h.written)
            assertEquals(listOf(1L,2L,3L),rows.map {it["sequence"]})
            assertEquals(listOf(7L,8L),rows.take(2).map {(it["fields"] as Map<*,*>)["eventId"]})
            assertEquals(text,(rows[2]["fields"] as Map<*,*>)["text"])
        }
    }
    @Test fun clock_changes_do_not_reorder_receipts_or_claim_hunter_capture_time() {
        ObservationJournal(temp.newFolder()).use {j ->
            j.record("event_summary",2000,100,mapOf("ageSeconds" to 20))
            j.record("event_summary",1000,200,mapOf("ageSeconds" to 21))
            assertTrue(j.awaitIdle());val copy=File(temp.root,"clock.jsonl");j.snapshot(copy);val rows=read(copy)
            assertEquals(listOf(2000L,1000L),rows.map {it["receivedEpochMs"]})
            assertEquals(listOf(100L,200L),rows.map {it["receivedMonotonicNs"]})
            assertFalse(rows.any {it.containsKey("capturedEpochMs")})
        }
    }
    @Test fun a_new_process_record_does_not_overwrite_an_existing_run() {
        val directory=temp.newFolder();val ids=mutableSetOf<String>()
        repeat(2) {ObservationJournal(directory).use {j ->
            ids+=j.health().runId;j.record("hello",1,1,emptyMap());assertTrue(j.awaitIdle())
        }}
        assertEquals(2,ids.size);assertEquals(2,directory.listFiles()!!.size)
    }
    @Test fun storage_limit_preserves_original_records_and_reports_omissions() {
        ObservationJournal(temp.newFolder(),maxFileBytes=700,maxDirectoryBytes=700).use {j ->
            repeat(10){j.record("event_summary",1,1,mapOf("text" to "x".repeat(200)))}
            assertTrue(j.awaitIdle());val h=j.health();assertTrue(h.written>0);assertTrue(h.dropped>0)
            assertEquals(0L,h.failed);assertEquals(10L,h.written+h.dropped+h.failed+h.pending)
            assertNotNull(h.problem);val copy=File(temp.root,"limited.jsonl");j.snapshot(copy)
            assertEquals(h.written,read(copy).size.toLong());assertTrue(copy.length()<=700)
        }
    }
    @Test fun io_failure_stops_append_and_remains_visible() {
        val directory=temp.newFile()
        ObservationJournal(directory).use {j ->
            repeat(4){j.record("event_summary",1,1,emptyMap())};assertTrue(j.awaitIdle())
            val h=j.health();assertEquals(0L,h.written);assertEquals(1L,h.failed);assertEquals(3L,h.dropped)
            assertNotNull(h.problem);assertEquals(0L,h.pending)
        }
    }
    @Test fun a_snapshot_is_a_complete_prefix_even_as_receipts_continue() {
        ObservationJournal(temp.newFolder()).use {j ->
            j.record("hello",1,1,emptyMap());assertTrue(j.awaitIdle())
            repeat(50){j.record("event_summary",it.toLong(),it.toLong(),mapOf("eventId" to it))}
            val copy=File(temp.root,"prefix.jsonl");val h=j.snapshot(copy);val rows=read(copy)
            assertEquals(h.written,rows.size.toLong());assertEquals((1L..h.written).toList(),rows.map {it["sequence"]})
        }
    }
}
