package com.projectrevivesolutions.layertime.controller.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectrevivesolutions.layertime.controller.ai.*
import com.projectrevivesolutions.layertime.controller.ai.runtime.BundledModelFactory
import com.projectrevivesolutions.layertime.controller.ui.theme.LtColors
import kotlinx.coroutines.*

@Composable internal fun FieldShadowPanel(input:FieldShadowTasks.Snapshot,onDismiss:()->Unit) {
    val context=LocalContext.current
    val provider=remember {BundledModelFactory.development(context)}
    val scope=rememberCoroutineScope()
    var job by remember {mutableStateOf<Job?>(null)}
    var busy by remember {mutableStateOf(false)}
    var status by remember {mutableStateOf("")}
    var result by remember {mutableStateOf<FieldShadowExperiment.Result?>(null)}
    var raw by remember {mutableStateOf(false)}
    val current by com.projectrevivesolutions.layertime.controller.state.ControllerRepository.latestFieldSnapshot.collectAsState()
    DisposableEffect(provider){onDispose {job?.cancel();(provider as? AutoCloseable)?.close()}}
    BuilderModal(onDismiss) {
        Text("FIELD AI Â· EXPERIMENTAL",color=LtColors.Amber,fontSize=16.sp)
        Text("This unqualified model can be wrong. Its explanation does not change alerts or verified evidence checks.",color=LtColors.Amber,fontSize=13.sp)
        Text("Hunter event #${input.eventId}, session ${input.sessionId}",color=LtColors.Cyan,fontSize=14.sp)
        Text("${input.facts["detector"]} Â· ${input.facts["radioDomain"]} Â· channel ${input.facts["channel"]}\nReported sightings: ${input.facts["reportedSightings"]} Â· age ${input.facts["reportedAgeSeconds"]} s\nObserved identifier: ${input.facts["observedIdentifier"] ?: "not received"}",color=LtColors.Ivory,fontSize=13.sp)
        Text("Saved observation received by Vixen at ${java.text.DateFormat.getTimeInstance().format(java.util.Date(input.summaryReceivedEpochMs))}.",color=LtColors.Grey,fontSize=12.sp)
        if(current?.sha256!=input.sha256)Text("The live input has changed. This explanation stays tied to the selected snapshot.",color=LtColors.Amber,fontSize=13.sp)
        val available=provider.availability().state==ModelState.READY
        if(!available)Text(provider.availability().reason ?: "The experimental model is unavailable.",color=LtColors.Amber,fontSize=13.sp)
        SciFiPrimary(if(busy)"READING THIS SNAPSHOTâ€¦" else "RUN EXPERIMENTAL EXPLANATION",available && !busy,large=true) {
            busy=true;status="Loading and reading this saved inputâ€¦";result=null;raw=false
            job=scope.launch {
                try {result=FieldShadowExperiment.run(provider,input)
                    status=if(result?.explanation!=null)"Generated in ${"%.1f".format(result!!.milliseconds/1000.0)} seconds. Review against the recorded facts."
                        else if(result?.reportProblems?.isNotEmpty()==true)"The response omitted or contradicted required report facts. It was saved for review."
                        else "The response failed format/reference checks. It was saved for review."}
                catch(ex:TimeoutCancellationException){status="The model exceeded the one-minute model limit."}
                catch(ex:CancellationException){status="Stopped.";throw ex}
                catch(ex:Exception){status="The explanation could not finish. The saved observations remain available."}
                finally{busy=false}
            }
        }
        if(busy)SciFiOutline("STOP",true){job?.cancel()}
        if(status.isNotBlank())Text(status,color=LtColors.Cyan,fontSize=13.sp)
        result?.explanation?.let {a->
            Text(a.headline,color=LtColors.Amber,fontSize=17.sp)
            for(value in listOf(a.significance,a.limitations,a.passiveNextStep))Text(value,color=LtColors.Ivory,fontSize=14.sp)
        }
        result?.let {r->SciFiOutline(if(raw)"HIDE RAW RESPONSE" else "SHOW RAW RESPONSE",true){raw=!raw}
            if(raw)Text(r.raw,color=LtColors.Grey,fontSize=12.sp)}
        Text("The exact input, output, model identity and elapsed time are recorded locally. They are not approved training data.",color=LtColors.Grey,fontSize=12.sp)
    }
}
