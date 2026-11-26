package com.projectrevivesolutions.layertime.controller.metrics
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import java.io.File
import java.util.UUID
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong

/** Independent bounded observer. Nothing in the product reads these records to make a decision. */
class KpiRecorder(private val dir:File,val clockId:String,private val clock:()->Long=System::nanoTime,
    private val capacity:Int=512,private val maxFileBytes:Long=16L*1024*1024,
    private val maxDirectoryBytes:Long=128L*1024*1024,
    private val append:(File,ByteArray)->Unit={f,b->f.appendBytes(b)}):AutoCloseable {
    val runId=UUID.randomUUID().toString()
    private val seen=AtomicLong();private val written=AtomicLong();private val dropped=AtomicLong();private val failed=AtomicLong()
    private val enqueueNs=AtomicLong();private val writerNs=AtomicLong();@Volatile private var stopped=false
    private val file=File(dir,"kpi-$runId.jsonl")
    private val worker=ThreadPoolExecutor(1,1,0,TimeUnit.MILLISECONDS,ArrayBlockingQueue<Runnable>(capacity),
        {t->Thread(t,"Vixen KPI observer").apply {isDaemon=true;priority=Thread.MIN_PRIORITY}})
    init {require(capacity>0 && maxFileBytes>0 && maxDirectoryBytes>=maxFileBytes)}
    fun now()=clock()
    fun health()=mapOf("runId" to runId,"recordsOffered" to seen.get(),"written" to written.get(),"dropped" to dropped.get(),
        "failed" to failed.get(),"pending" to worker.queue.size,"stopped" to stopped,"enqueueTotalNs" to enqueueNs.get(),"writerElapsedNs" to writerNs.get())
    private fun saveHealth() {runCatching {File(dir,"kpi-$runId.health.json").writeText(FlowJson.write(health()))}}
    fun record(kind:String,key:String?=null,fields:Map<String,Any?> = emptyMap()) {
        val start=System.nanoTime()
        try {
            val seq=seen.incrementAndGet()
            if(stopped){dropped.incrementAndGet();return}
            if(!kind.matches(Regex("[a-z0-9_]{1,64}")) || fields.size>32){dropped.incrementAndGet();return}
            val safe=fields.mapValues {(_,v)->when(v){null,is Boolean,is Number->v;is String->v.take(256);else->"unsupported_metric_value"}}
            val time=clock();val epoch=System.currentTimeMillis()
            try {worker.execute {
                val began=System.nanoTime()
                try {
                    if(stopped){dropped.incrementAndGet();return@execute}
                    check(dir.isDirectory || dir.mkdirs())
                    val bytes=(FlowJson.write(mapOf("schema" to "vixen-kpi/1","runId" to runId,"clockId" to clockId,"sequence" to seq,
                        "monotonicNs" to time,"epochMs" to epoch,"kind" to kind,"eventKey" to key,"fields" to safe))+"\n").toByteArray()
                    val total=dir.listFiles().orEmpty().filter {it.extension=="jsonl"}.sumOf {it.length()}
                    if(file.length()+bytes.size>maxFileBytes || total+bytes.size>maxDirectoryBytes){stopped=true;dropped.incrementAndGet()}
                    else {append(file,bytes);completeLength=file.length();written.incrementAndGet()}
                } catch(ex:Exception){failed.incrementAndGet();stopped=true}
                finally {writerNs.addAndGet(System.nanoTime()-began);if(seq%32==0L || stopped)saveHealth()}
            }} catch(ex:RejectedExecutionException){dropped.incrementAndGet()}
        } catch(ex:Exception){failed.incrementAndGet()}
        finally {enqueueNs.addAndGet(System.nanoTime()-start)}
    }
    @Volatile private var completeLength=0L
    fun snapshot(destination:File):File? {
        val length=completeLength;if(length==0L)return null
        check(destination.canonicalFile!=file.canonicalFile)
        destination.parentFile?.mkdirs();check(destination.createNewFile())
        file.inputStream().use {i->destination.outputStream().use {o->var left=length;val b=ByteArray(65536)
            while(left>0){val n=i.read(b,0,minOf(left,b.size.toLong()).toInt());check(n>0);o.write(b,0,n);left-=n}}}
        return destination
    }
    fun checkpoint() {runCatching {worker.execute {saveHealth()}}}
    fun awaitIdle(ms:Long=5000):Boolean {val latch=CountDownLatch(1);return runCatching {worker.execute {latch.countDown()};latch.await(ms,TimeUnit.MILLISECONDS)}.getOrDefault(false)}
    override fun close() {worker.shutdown();worker.awaitTermination(5,TimeUnit.SECONDS);saveHealth()}
}
