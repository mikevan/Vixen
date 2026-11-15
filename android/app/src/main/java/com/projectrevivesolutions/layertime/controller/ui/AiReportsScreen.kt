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
import com.projectrevivesolutions.layertime.controller.signatures.SignatureReportStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable fun AiReportsScreen() {
    val context=LocalContext.current;val scope=rememberCoroutineScope()
    val reports=rememberSignatureReports()
    var archived by remember {mutableStateOf(false)}
    var selectedId by remember {mutableStateOf<String?>(null)}
    var confirming by remember {mutableStateOf<SignatureReportStore.Report?>(null)}
    var exporting by remember {mutableStateOf<SignatureReportStore.Report?>(null)}
    val export=rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")){uri->
        val report=exporting
        if(uri!=null && report!=null)scope.launch {
            val result=withContext(Dispatchers.IO){runCatching {context.contentResolver.openOutputStream(uri,"wt")!!.use {it.write(report.json.toByteArray())}}}
            Toast.makeText(context,if(result.isSuccess)"Report exported" else "Could not export report",Toast.LENGTH_LONG).show()
        }
    }
    Column(Modifier.fillMaxSize().padding(12.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Text("AI Reports",style=MaterialTheme.typography.titleLarge)
        Row {
            TextButton(onClick={archived=false}){Text(if(!archived)"Active · selected" else "Active")}
            TextButton(onClick={archived=true}){Text(if(archived)"Archived · selected" else "Archived")}
        }
        val visible=reports.filter {it.archived==archived}
        if(visible.isEmpty())Text(if(archived)"No archived reports" else "No active reports")
        LazyColumn(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(8.dp)) {
            items(visible,key={it.id}){report->Card(onClick={selectedId=report.id},modifier=Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                    Text(reportTime(report.completedEpochMs),style=MaterialTheme.typography.titleSmall)
                    Text("Report ${report.id.take(8)} · Hunter session ${report.sessions}",style=MaterialTheme.typography.labelSmall)
                    Text(report.status,style=MaterialTheme.typography.labelSmall)
                    Text(reportWording(report.finding ?: "No accepted finding saved."),style=MaterialTheme.typography.bodySmall)
                }
            }}
        }
    }
    reports.find {it.id==selectedId}?.let {report->AlertDialog(onDismissRequest={selectedId=null},title={Text("Report ${report.id.take(8)}")},text={LazyColumn {item {
        Text(reportTime(report.completedEpochMs));Text("Hunter session ${report.sessions}")
        Text(report.status);Text(reportWording(report.finding ?: "No accepted AI finding was saved."))
        TextButton(onClick={exporting=report;export.launch("vixen-ai-report-${report.id.take(12)}.json")}){Text("Export report")}
        if(report.archived)TextButton(onClick={scope.launch {
            val result=withContext(Dispatchers.IO){runCatching {SignatureReportStore(context.filesDir).archive(report.id,false)}}
            if(result.isSuccess)selectedId=null else Toast.makeText(context,"Could not restore report",Toast.LENGTH_LONG).show()
        }}){Text("Restore from archive")}
        else TextButton(onClick={confirming=report}){Text("Archive report")}
    }}},confirmButton={TextButton(onClick={selectedId=null}){Text("Close")}})}
    confirming?.let {ArchiveReportConfirmation(it){confirming=null;selectedId=null}}
}
