package com.projectrevivesolutions.layertime.controller.state

import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import java.io.File
import java.util.UUID
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/** Append-only receipt ledger, before UI grouping. Disk work never runs on the BLE handler. */
class ObservationJournal(
    private val directory: File,
    private val maxFileBytes: Long = 256L * 1024 * 1024,
    private val maxDirectoryBytes: Long = 1024L * 1024 * 1024,
    queueCapacity: Int = 2048,
    private val onHealth: (Health) -> Unit = {},
) : AutoCloseable {
    data class Health(val runId: String = "", val received: Long = 0, val written: Long = 0,
        val dropped: Long = 0, val failed: Long = 0, val pending: Long = 0, val problem: String? = null, val lastWrittenSequence:Long = 0)
    data class Receipt(val runId:String,val sequence:Long,val queued:Boolean)
    private val runId = UUID.randomUUID().toString()
    private val file = File(directory, "vixen-observations-$runId.jsonl")
    private val received = AtomicLong(); private val written = AtomicLong()
    private val dropped = AtomicLong(); private val failed = AtomicLong(); private val pending = AtomicLong()
    @Volatile private var problem: String? = null
    private val fileLock = Any()
    private var completeLength=0L
    private val lastWrittenSequence=AtomicLong()
    @Volatile private var stopped=false
    private val worker = ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
        ArrayBlockingQueue<Runnable>(queueCapacity), { task -> Thread(task,"Vixen field record").apply { isDaemon=true } })
    init { require(maxFileBytes > 0 && maxDirectoryBytes >= maxFileBytes && queueCapacity > 0) }
    fun health() = Health(runId,received.get(),written.get(),dropped.get(),failed.get(),pending.get(),problem,lastWrittenSequence.get())
    private fun publish() { onHealth(health()) }

    @Synchronized fun record(kind: String, epochMs: Long, monotonicNs: Long, fields: Map<String,Any?>): Receipt {
        val sequence = received.incrementAndGet()
        val line = FlowJson.write(linkedMapOf("schema" to "vixen-observation/1", "runId" to runId,
            "sequence" to sequence, "receivedEpochMs" to epochMs, "receivedMonotonicNs" to monotonicNs,
            "kind" to kind, "fields" to fields)) + "\n"
        pending.incrementAndGet()
        return try {
            worker.execute {
                try {
                    synchronized(fileLock) {
                        if(stopped) {dropped.incrementAndGet();return@synchronized}
                        check(directory.isDirectory || directory.mkdirs())
                        val bytes=line.toByteArray(Charsets.UTF_8)
                        val total=directory.listFiles().orEmpty().filter { it.name.startsWith("vixen-observations-") && it.extension=="jsonl" }.sumOf {it.length()}
                        if(file.length()+bytes.size > maxFileBytes || total+bytes.size > maxDirectoryBytes) {
                            dropped.incrementAndGet(); stopped=true; problem="Field recording storage limit reached. Export and preserve the records."
                        } else {
                            file.appendBytes(bytes); completeLength=file.length(); written.incrementAndGet();lastWrittenSequence.set(sequence)
                        }
                    }
                } catch(ex:Exception) {failed.incrementAndGet();stopped=true;problem="Field recording stopped after a write error. Some observations were not saved."}
                finally {pending.decrementAndGet();publish()}
            }
            publish();Receipt(runId,sequence,true)
        } catch(ex:RejectedExecutionException) {
            pending.decrementAndGet();dropped.incrementAndGet();problem="Some field records were not queued for saving.";publish();Receipt(runId,sequence,false)
        }
    }
    /** A barrier for tests/controlled shutdown; normal receipt never waits on it. */
    fun awaitIdle(timeoutMs: Long = 5000): Boolean {
        val done=CountDownLatch(1)
        return try {worker.execute {done.countDown()};done.await(timeoutMs,TimeUnit.MILLISECONDS)}
        catch(ex:RejectedExecutionException) {false}
    }
    /** Copy a complete written prefix while the worker continues appending. No old file is removed. */
    fun snapshot(destination: File): Health {
        val (length, state) = synchronized(fileLock) {completeLength to health()}
        check(length > 0) {"No field records have been saved yet."}
        check(destination.canonicalFile != file.canonicalFile)
        destination.parentFile?.mkdirs()
        file.inputStream().use {input -> destination.outputStream().use {output ->
            var left=length;val buffer=ByteArray(64*1024)
            while(left>0) {val n=input.read(buffer,0,minOf(buffer.size.toLong(),left).toInt());check(n>0);output.write(buffer,0,n);left-=n}
        }}
        return state
    }
    override fun close() {worker.shutdown();worker.awaitTermination(5,TimeUnit.SECONDS)}
}
