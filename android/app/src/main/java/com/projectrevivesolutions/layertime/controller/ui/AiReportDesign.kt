package com.projectrevivesolutions.layertime.controller.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val ReportInk=Color(0xFFE5EEF2)
internal val ReportCyan=Color(0xFF72EDFF)
internal val ReportMuted=Color(0xFF8198A4)
internal val ReportType=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=12.sp,lineHeight=16.sp)

@Composable internal fun AiReportWindow(modifier:Modifier=Modifier,content:@Composable ColumnScope.()->Unit) {
    OpsConnectionHousing(modifier) {
        CompositionLocalProvider(LocalContentColor provides ReportInk,LocalTextStyle provides ReportType) {
            Column(Modifier.fillMaxWidth().padding(start=18.dp,end=18.dp,top=20.dp,bottom=24.dp),
                verticalArrangement=Arrangement.spacedBy(6.dp),content=content)
        }
    }
}
@Composable internal fun AiReportHeading(text:String) {
    Text(text,color=ReportCyan,style=ReportType.copy(fontSize=12.sp,letterSpacing=.8.sp))
    HorizontalDivider(color=ReportCyan.copy(alpha=.22f),thickness=.5.dp)
}
@Composable internal fun AiReportAction(text:String,enabled:Boolean=true,onClick:()->Unit) {
    SciFiOutline(text,enabled,onClick=onClick)
}
