package com.projectrevivesolutions.layertime.controller.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    OpsTelemetryHousing(Modifier.fillMaxSize(),borderBack=LocalPageBack.current) {
    Column(Modifier.fillMaxSize().padding(start=18.dp,end=18.dp,top=20.dp,bottom=24.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp),verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
            PageBackControl()
            Text("AI REPORTS",color=ReportCyan,style=ReportType.copy(letterSpacing=.8.sp))
        }
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            AiReportAction(if(!archived)"Active •" else "Active"){archived=false}
            AiReportAction(if(archived)"Archived •" else "Archived"){archived=true}
        }
        val visible=reports.filter {it.archived==archived}
        if(visible.isEmpty())Text(if(archived)"No archived reports" else "No active reports")
        LazyColumn(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(8.dp)) {
            items(visible,key={it.id}){report->Column(Modifier.fillMaxWidth().padding(vertical=8.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                    Text(reportTime(report.completedEpochMs),style=MaterialTheme.typography.titleSmall)
                    Text("Report ${report.id.take(8)} · Hunter session ${report.sessions}",style=MaterialTheme.typography.labelSmall)
                    Text(report.status,color=ReportCyan,style=MaterialTheme.typography.labelSmall)
                    Text(reportWording(report.finding ?: "No accepted finding saved."),style=MaterialTheme.typography.bodySmall)
                    AiReportAction("Open report"){selectedId=report.id}
                    HorizontalDivider(color=ReportCyan.copy(alpha=.22f),thickness=.5.dp)
            }}
        }
    }
    }
    reports.find {it.id==selectedId}?.let {report->AlertDialog(containerColor=Color(0xFF071E2A),titleContentColor=ReportCyan,textContentColor=ReportInk,shape=CutCornerShape(12.dp),modifier=Modifier.border(.8.dp,ReportCyan.copy(alpha=.5f),CutCornerShape(12.dp)),onDismissRequest={selectedId=null},title={Text("Report ${report.id.take(8)}")},text={LazyColumn {item {
        Text(reportTime(report.completedEpochMs));Text("Hunter session ${report.sessions}")
        Text(report.status);Text(reportWording(report.finding ?: "No accepted AI finding was saved."))
        AiReportAction("Export report"){exporting=report;export.launch("vixen-ai-report-${report.id.take(12)}.json")}
        if(report.archived)AiReportAction("Restore from archive",onClick={scope.launch {
            val result=withContext(Dispatchers.IO){runCatching {SignatureReportStore(context.filesDir).archive(report.id,false)}}
            if(result.isSuccess)selectedId=null else Toast.makeText(context,"Could not restore report",Toast.LENGTH_LONG).show()
        }})
        else AiReportAction("Archive report"){confirming=report}
    }}},confirmButton={AiReportAction("Close"){selectedId=null}})}
    confirming?.let {ArchiveReportConfirmation(it){confirming=null;selectedId=null}}
}
