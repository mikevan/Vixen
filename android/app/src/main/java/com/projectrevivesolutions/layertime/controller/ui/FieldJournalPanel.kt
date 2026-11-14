package com.projectrevivesolutions.layertime.controller.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.projectrevivesolutions.layertime.controller.flow.FlowJson
import com.projectrevivesolutions.layertime.controller.state.FieldObservationJournal
import com.projectrevivesolutions.layertime.controller.ui.theme.LtColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable fun FieldJournalPanel() {
    val health by FieldObservationJournal.health.collectAsState()
    val snapshot by com.projectrevivesolutions.layertime.controller.state.ControllerRepository.latestFieldSnapshot.collectAsState()
    var shadowInput by remember {mutableStateOf<com.projectrevivesolutions.layertime.controller.ai.FieldShadowTasks.Snapshot?>(null)}
    val latest by com.projectrevivesolutions.layertime.controller.state.ControllerRepository.latestFieldAnalysis.collectAsState()
    val connected by com.projectrevivesolutions.layertime.controller.state.ControllerRepository.conn.collectAsState()
    val stale by com.projectrevivesolutions.layertime.controller.state.ControllerRepository.linkStale.collectAsState()
    val context=LocalContext.current;val scope=rememberCoroutineScope()
    var exporting by remember {mutableStateOf(false)}
    var notice by remember {mutableStateOf<String?>(null)}
    shadowInput?.let {FieldShadowPanel(it){shadowInput=null}}
    Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(4.dp)) {
        Text("FIELD RECORD",color=LtColors.Cyan,fontSize=12.sp)
        Text(if(health.runId.isBlank()) "Starts with the Hunter connection service."
            else "${health.written} saved, ${health.pending} waiting, ${health.dropped+health.failed} not saved",
            color=if(health.dropped+health.failed>0)LtColors.Amber else LtColors.Grey,fontSize=12.sp)
        latest?.let {Text("Evidence checks: $it",color=LtColors.Grey,fontSize=12.sp)}
        if(latest!=null && (connected!=com.projectrevivesolutions.layertime.controller.model.ConnState.CONNECTED || stale))
            Text("Live feed unavailable; showing the last received evidence checks.",color=LtColors.Amber,fontSize=12.sp)
        health.problem?.let {Text(it,color=LtColors.Amber,fontSize=12.sp)}
        Text("Individual Hunter records, before display grouping. This is what Vixen received; it does not prove Hunter captured or saved everything.",
            color=LtColors.Grey,fontSize=12.sp)
        if(com.projectrevivesolutions.layertime.controller.BuildConfig.DEBUG)
            SciFiOutline("EXPERIMENTAL AI FOR LATEST OBSERVATION",snapshot!=null){shadowInput=snapshot}
        SciFiOutline("EXPORT FIELD RECORD",health.written>0 && !exporting) {
            exporting=true;notice=null
            scope.launch {
                try {
                    val files=withContext(Dispatchers.IO) {
                        val dir=File(context.filesDir,"exports").apply {mkdirs()}
                        val base="field-${health.runId}-${System.currentTimeMillis()}"
                        val record=File(dir,"$base.jsonl");val snapshot=FieldObservationJournal.snapshot(record)
                        val manifest=File(dir,"$base.json")
                        manifest.writeText(FlowJson.write(mapOf("schema" to "vixen-field-export/1",
                            "runId" to snapshot.runId,"recordsRequested" to snapshot.received,"written" to snapshot.written,
                            "dropped" to snapshot.dropped,"failed" to snapshot.failed,"pending" to snapshot.pending,
                            "problem" to snapshot.problem,"recordFile" to record.name,
                            "clockBasis" to "Vixen receipt wall clock and monotonic clock; not synchronized RF capture time")))
                        listOf(record,manifest)+com.projectrevivesolutions.layertime.controller.metrics.KpiTelemetry.export(dir,base)
                    }
                    val uris=ArrayList(files.map {FileProvider.getUriForFile(context,"${context.packageName}.fileprovider",it)})
                    context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                        type="application/octet-stream";putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    },"Export field record"))
                } catch(ex:kotlinx.coroutines.CancellationException) {throw ex}
                catch(ex:Exception) {notice="Could not export the field record. The original remains on this phone."}
                finally {exporting=false}
            }
        }
        notice?.let {Text(it,color=LtColors.Amber,fontSize=12.sp)}
    }
}
