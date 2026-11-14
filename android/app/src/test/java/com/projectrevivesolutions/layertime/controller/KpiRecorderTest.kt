package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.metrics.KpiRecorder
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files
class KpiRecorderTest {
 @Test fun records_are_ordered_and_do_not_modify_caller_data() {
  val dir=Files.createTempDirectory("kpi").toFile();val data=mutableMapOf<String,Any?>("count" to 2)
  KpiRecorder(dir,"clock",{42L}).use {r->r.record("observation_received","event",data);data["count"]=9;assertTrue(r.awaitIdle())}
  val row=FlowJson.parse(dir.listFiles()!!.single {it.extension=="jsonl"}.readText().trim()) as Map<*,*>
  assertEquals(42L,row["monotonicNs"]);assertEquals(2L,(row["fields"] as Map<*,*>)["count"])
 }
 @Test fun storage_failure_is_visible_and_cannot_escape_to_product() {
  val file=Files.createTempFile("kpi-not-directory","tmp").toFile()
  KpiRecorder(file,"clock").use {r->r.record("warning_submitted");assertTrue(r.awaitIdle());assertTrue((r.health()["failed"] as Long)>0)}
 }
 @Test fun full_queue_drops_measurements_without_backpressure() {
  val entered=java.util.concurrent.CountDownLatch(1);val release=java.util.concurrent.CountDownLatch(1)
  val dir=Files.createTempDirectory("kpi-blocked").toFile()
  val recorder=KpiRecorder(dir,"clock",capacity=1,append={f,b->entered.countDown();release.await();f.appendBytes(b)})
  try {
   recorder.record("first");assertTrue(entered.await(5,java.util.concurrent.TimeUnit.SECONDS))
   recorder.record("second");recorder.record("third")
   assertEquals(1L,release.count);assertTrue((recorder.health()["dropped"] as Long)>0)
  } finally {release.countDown();recorder.awaitIdle();recorder.close()}
 }
 @Test fun disk_limit_does_not_delete_or_replace_existing_records() {
  val dir=Files.createTempDirectory("kpi-limit").toFile();val kept=java.io.File(dir,"old.jsonl").apply {writeText("original")}
  KpiRecorder(dir,"clock",maxFileBytes=1,maxDirectoryBytes=8).use {r->r.record("x");assertTrue(r.awaitIdle());assertTrue((r.health()["dropped"] as Long)>0)}
  assertEquals("original",kept.readText())
 }
}
