package com.projectrevivesolutions.layertime.controller.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectrevivesolutions.layertime.controller.signatures.LiveSignatureAnalysis
import com.projectrevivesolutions.layertime.controller.ui.theme.LtColors
import com.projectrevivesolutions.layertime.controller.state.ControllerRepository
import com.projectrevivesolutions.layertime.controller.model.ConnState
@Composable fun LiveSignaturePanel() {
    val state by LiveSignatureAnalysis.state.collectAsState()
    val connection by ControllerRepository.conn.collectAsState()
    Column(Modifier.fillMaxWidth().padding(10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
        Text(state.status,color=LtColors.Cyan,fontSize=12.sp)
        state.finding?.let {Text(it,color=LtColors.Grey,fontSize=12.sp)}
        if(state.finding!=null && connection!=ConnState.CONNECTED)Text("Recorded assessment; live feed disconnected",color=LtColors.Amber,fontSize=12.sp)
    }
}
