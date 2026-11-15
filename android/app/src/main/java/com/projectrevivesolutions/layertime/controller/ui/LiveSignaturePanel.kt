package com.projectrevivesolutions.layertime.controller.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectrevivesolutions.layertime.controller.signatures.LiveSignatureAnalysis
import com.projectrevivesolutions.layertime.controller.signatures.SignatureReportStore
import com.projectrevivesolutions.layertime.controller.ui.theme.LtColors
import com.projectrevivesolutions.layertime.controller.state.ControllerRepository
import com.projectrevivesolutions.layertime.controller.model.ConnState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Date

private fun reportWording(text:String)=text.replace("Deauthentication and EAPOL observations", "Wi-Fi disconnect messages and connection-handshake traffic").replace("Remote ID broadcast","drone identification broadcast").replace("Remote ID preceded","A drone identification broadcast preceded").replace("an unverified drone identification broadcast","a drone identification broadcast").replace(" This is a candidate sequence for review; it does not prove an outage, a coordinated attack, or hostile drone intent.", "")

private fun reportTime(time:Long?)=time?.let {DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(Date(it))} ?: "Creation time unavailable"

@Composable fun LiveSignaturePanel() {
    val state by LiveSignatureAnalysis.state.collectAsState()
    val connection by ControllerRepository.conn.collectAsState()
    val context=LocalContext.current
    val store=remember(context){SignatureReportStore(context.filesDir)}
    val scope=rememberCoroutineScope()
    var reports by remember {mutableStateOf(emptyList<SignatureReportStore.Report>())}
    var open by remember {mutableStateOf(false)}
    var archived by remember {mutableStateOf(false)}
    var revision by remember {mutableStateOf(0)}
    var selected by remember {mutableStateOf<SignatureReportStore.Report?>(null)}
    var exporting by remember {mutableStateOf<SignatureReportStore.Report?>(null)}
    val export=rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) {uri ->
        val report=exporting
        if(uri!=null && report!=null)scope.launch {
            val result=withContext(Dispatchers.IO){runCatching {context.contentResolver.openOutputStream(uri,"wt")!!.use {it.write(report.json.toByteArray())}}}
            Toast.makeText(context,if(result.isSuccess)"Report exported" else "Could not export report",Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(state,open,revision){reports=withContext(Dispatchers.IO){store.list()}}
    val current=state.key?.let {key->reports.find {it.id==SignatureReportStore.idForKey(key)}}
    Column(Modifier.fillMaxWidth().padding(10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
        Text(state.status,color=LtColors.Cyan,fontSize=12.sp)
        current?.let {Text("Saved: ${reportTime(it.completedEpochMs)} · ${it.id.take(8)}${if(it.archived) " · Archived" else ""}",color=LtColors.Grey,fontSize=12.sp)}
        state.finding?.let {Text(reportWording(it),color=LtColors.Grey,fontSize=12.sp)}
        if(state.finding!=null && connection!=ConnState.CONNECTED)Text("Recorded assessment; live feed disconnected",color=LtColors.Amber,fontSize=12.sp)
        TextButton(onClick={open=true}){Text("AI reports (${reports.count { !it.archived }})")}
    }
    if(open)AlertDialog(onDismissRequest={open=false},title={Text("Saved AI reports")},text={Column {
        Text("Times show when analysis completed on this phone. Archived reports remain saved.")
        TextButton(onClick={archived=!archived}){Text(if(archived)"Show active reports" else "Show archive")}
        val visible=reports.filter {it.archived==archived}
        if(visible.isEmpty())Text(if(archived)"No archived reports" else "No active reports")
        LazyColumn(Modifier.heightIn(max=400.dp)){items(visible,key={it.id}){report->
            TextButton(onClick={selected=report}){Column(Modifier.fillMaxWidth()){
                Text(reportTime(report.completedEpochMs));Text("Report ${report.id.take(8)} · Hunter session ${report.sessions}")
                Text(report.status)
            }}
        }}
    }},confirmButton={TextButton(onClick={open=false}){Text("Close")}})
    selected?.let {report->AlertDialog(onDismissRequest={selected=null},title={Text("Report ${report.id.take(8)}")},text={LazyColumn {item {
        Text(reportTime(report.completedEpochMs));Text("Hunter session ${report.sessions}")
        Text(report.status);Text(reportWording(report.finding ?: "No accepted AI finding was saved."))
        TextButton(onClick={exporting=report;export.launch("vixen-ai-report-${report.id.take(12)}.json")}){Text("Export report")}
        TextButton(onClick={scope.launch {
            val result=withContext(Dispatchers.IO){runCatching {store.archive(report.id,!report.archived)}}
            if(result.isSuccess){revision++;selected=null}else Toast.makeText(context,"Could not update archive",Toast.LENGTH_LONG).show()
        }}){Text(if(report.archived)"Restore from archive" else "Archive report")}
    }}},confirmButton={TextButton(onClick={selected=null}){Text("Close")}})}
}
