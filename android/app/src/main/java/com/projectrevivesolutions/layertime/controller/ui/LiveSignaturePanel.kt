package com.projectrevivesolutions.layertime.controller.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.projectrevivesolutions.layertime.controller.signatures.LiveSignatureAnalysis
import com.projectrevivesolutions.layertime.controller.signatures.SignatureReportStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Date

internal fun reportWording(text:String)=text.replace("Deauthentication and EAPOL observations", "Wi-Fi disconnect messages and connection-handshake traffic").replace("Remote ID broadcast","drone identification broadcast").replace("Remote ID preceded","A drone identification broadcast preceded").replace("an unverified drone identification broadcast","a drone identification broadcast").replace(" This is a candidate sequence for review; it does not prove an outage, a coordinated attack, or hostile drone intent.", "")
internal fun reportTime(time:Long?)=time?.let {DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(Date(it))} ?: "Creation time unavailable"

@Composable internal fun rememberSignatureReports():List<SignatureReportStore.Report> {
    val context=LocalContext.current
    val state by LiveSignatureAnalysis.state.collectAsState()
    val revision by SignatureReportStore.changes.collectAsState()
    var reports by remember {mutableStateOf(emptyList<SignatureReportStore.Report>())}
    LaunchedEffect(state,revision){reports=withContext(Dispatchers.IO){SignatureReportStore(context.filesDir).list()}}
    return reports
}

@Composable internal fun ArchiveReportConfirmation(report:SignatureReportStore.Report,onDismiss:()->Unit) {
    val context=LocalContext.current;val scope=rememberCoroutineScope()
    var saving by remember {mutableStateOf(false)}
    AlertDialog(containerColor=Color(0xFF071E2A),titleContentColor=ReportCyan,textContentColor=ReportInk,shape=CutCornerShape(12.dp),modifier=Modifier.border(.8.dp,ReportCyan.copy(alpha=.5f),CutCornerShape(12.dp)),onDismissRequest={if(!saving)onDismiss()},title={Text("Archive this report?")},
        text={Text("It will leave OPS and remain available in AI Reports. The report and its evidence will be kept.")},
        dismissButton={AiReportAction("Cancel",!saving,onDismiss)},
        confirmButton={AiReportAction("Archive",!saving,onClick={saving=true;scope.launch {
            val result=withContext(Dispatchers.IO){runCatching {SignatureReportStore(context.filesDir).archive(report.id,true)}}
            saving=false
            if(result.isSuccess)onDismiss()else Toast.makeText(context,"Could not archive report",Toast.LENGTH_LONG).show()
        }})})
}

@Composable fun LiveSignaturePanel() {
    val state by LiveSignatureAnalysis.state.collectAsState()
    val reports=rememberSignatureReports()
    val store=SignatureReportStore(LocalContext.current.filesDir)
    val current=store.current(reports,state.key)
    var confirming by remember {mutableStateOf<SignatureReportStore.Report?>(null)}
    AiReportWindow(Modifier.fillMaxWidth().padding(horizontal=10.dp,vertical=6.dp)) {
            AiReportHeading("CURRENT AI REPORT")
            if(current==null)Text(if(state.status.startsWith("Analyzing"))state.status else "No current report",style=MaterialTheme.typography.bodySmall)
            else {
                Text(reportTime(current.completedEpochMs),style=MaterialTheme.typography.labelSmall)
                Text("Report ${current.id.take(8)} · Hunter session ${current.sessions}",style=MaterialTheme.typography.labelSmall)
                Text(reportWording(current.finding ?: current.status),style=MaterialTheme.typography.bodySmall)
                if(state.status.startsWith("Analyzing"))Text(state.status,style=MaterialTheme.typography.labelSmall)
                AiReportAction("Archive"){confirming=current}
            }
    }
    confirming?.let {ArchiveReportConfirmation(it){confirming=null}}
}
