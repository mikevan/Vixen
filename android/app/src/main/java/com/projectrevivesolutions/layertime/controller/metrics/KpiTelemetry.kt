package com.projectrevivesolutions.layertime.controller.metrics
import android.content.*
import android.os.*
import android.net.*
import android.provider.Settings
import com.projectrevivesolutions.layertime.controller.BuildConfig
import kotlinx.coroutines.*
import java.io.File
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

object KpiTelemetry {
    @Volatile private var recorder:KpiRecorder?=null
    private val ids=AtomicLong()
    data class Stamp(val id:String,val beganNs:Long)
    @Synchronized fun start(context:Context) {
        if(recorder!=null)return
        runCatching {
            val app=context.applicationContext;val prefs=app.getSharedPreferences("kpi_identity",Context.MODE_PRIVATE)
            val install=prefs.getString("install",null) ?: UUID.randomUUID().toString().also {prefs.edit().putString("install",it).apply()}
            val boot=Settings.Global.getInt(app.contentResolver,Settings.Global.BOOT_COUNT,-1)
            val r=KpiRecorder(File(app.filesDir,"kpi-metrics"),"$install/boot-$boot/elapsedRealtimeNanos",SystemClock::elapsedRealtimeNanos)
            recorder=r
            mark("process_started",fields=mapOf("versionCode" to BuildConfig.VERSION_CODE,"versionName" to BuildConfig.VERSION_NAME,"package" to app.packageName))
            CoroutineScope(SupervisorJob()+Dispatchers.IO).launch {
                while(isActive) {
                    runCatching {
                        val battery=app.registerReceiver(null,IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                        val bm=app.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                        val cm=app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val caps=runCatching {cm.getNetworkCapabilities(cm.activeNetwork)}.getOrNull()
                        mark("resource_sample",fields=mapOf("batteryLevel" to battery?.getIntExtra(BatteryManager.EXTRA_LEVEL,-1),
                            "batteryScale" to battery?.getIntExtra(BatteryManager.EXTRA_SCALE,-1),"plugged" to battery?.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1),
                            "temperatureTenthsC" to battery?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1),
                            "chargeCounterUah" to bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER),
                            "validatedNetwork" to caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED),
                            "heapUsedBytes" to Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory(),"nativeHeapBytes" to Debug.getNativeHeapAllocatedSize()))
                        r.checkpoint()
                    }
                    delay(60000)
                }
            }
        }
    }
    fun mark(kind:String,key:String?=null,fields:Map<String,Any?> = emptyMap()) {runCatching {recorder?.record(kind,key,fields)}}
    fun eventKey(peer:String?,session:Int,event:Long):String = runCatching {
        MessageDigest.getInstance("SHA-256").digest("${peer?.lowercase()}/$session/$event".toByteArray()).joinToString(""){"%02x".format(it)}
    }.getOrDefault("unknown")
    fun begin(kind:String,fields:Map<String,Any?>):Stamp? = runCatching {
        val r=recorder ?: return null
        Stamp("${r.runId}:${ids.incrementAndGet()}",r.now()).also {mark(kind+"_started",it.id,fields)}
    }.getOrNull()
    fun end(stamp:Stamp?,status:String) {if(stamp!=null)runCatching {
        val r=recorder ?: return
        mark("ai_attempt_finished",stamp.id,mapOf("status" to status,"durationNs" to r.now()-stamp.beganNs,"validationScope" to "caller_contract_not_semantic_truth"))
    }}
    fun export(directory:File,base:String):List<File> = runCatching {
        val r=recorder ?: return emptyList()
        val record=r.snapshot(File(directory,"$base-kpi.jsonl")) ?: return emptyList()
        val health=File(directory,"$base-kpi-health.json").apply {writeText(com.projectrevivesolutions.layertime.controller.flow.FlowJson.write(r.health()))}
        listOf(record,health)
    }.getOrDefault(emptyList())
    fun observe(kind:String,fields:Map<String,Any?>) {
        runCatching {
            val session=(fields["sessionId"] as? Number)?.toInt() ?: -1
            val event=(fields["eventId"] as? Number)?.toLong()
            val key=event?.let {eventKey(fields["hunterAddress"] as? String,session,it)}
            when(kind) {
                "event_summary"->mark("observation_received",key,mapOf("session" to session,"eventId" to event,"detector" to fields["detector"],"count" to fields["count"],"confidence" to fields["confidence"],"ageSeconds" to fields["ageSeconds"]))
                "connection"->mark("connection_state",fields=mapOf("state" to fields["state"],"session" to session))
                "change_gap"->mark("transport_gap",fields=mapOf("session" to session,"changeSeq" to fields["changeSeq"]))
                "evidence_received"->mark("evidence_saved",key,mapOf("frames" to fields["frames"],"seen" to fields["seen"],"dropped" to fields["dropped"],"pcapSha256" to fields["pcapSha256"]))
                "evidence_transfer_failed","evidence_save_failed"->mark(kind,key)
                "event_analysis"->mark("triage_finished",key,mapOf("engine" to fields["engine"],"modelUsed" to fields["modelUsed"],"verdict" to fields["verdict"]))
            }
        }
    }
}
