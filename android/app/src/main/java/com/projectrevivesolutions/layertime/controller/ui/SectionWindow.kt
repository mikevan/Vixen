package com.projectrevivesolutions.layertime.controller.ui

import android.view.HapticFeedbackConstants
import android.media.AudioManager
import android.media.ToneGenerator
import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private object FrameRailFeedback {var pressedAt by mutableLongStateOf(0L)}

/** Recessed, fully enclosed back key inside its parent housing. */
@Composable
internal fun FrameBackControl(bottom:Boolean=false,compact:Boolean=false,onBack:()->Unit) {
    val pulse=remember {Animatable(0f)}
    val view=LocalView.current
    val tone=remember {runCatching {ToneGenerator(AudioManager.STREAM_SYSTEM,20)}.getOrNull()}
    DisposableEffect(tone){onDispose {tone?.release()}}
    val pressedAt=FrameRailFeedback.pressedAt
    LaunchedEffect(pressedAt) {
        if(pressedAt>0 && android.os.SystemClock.uptimeMillis()-pressedAt<400) {
            pulse.snapTo(1f);pulse.animateTo(.15f,tween(55));pulse.animateTo(1f,tween(55));pulse.animateTo(0f,tween(240))
        }
    }
    Box(Modifier.width(if(compact)64.dp else 96.dp).height(32.dp).clickable(role=Role.Button,onClickLabel="Back") {
        FrameRailFeedback.pressedAt=android.os.SystemClock.uptimeMillis()
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        if(Settings.System.getInt(view.context.contentResolver,Settings.System.SOUND_EFFECTS_ENABLED,1)==1)
            tone?.startTone(ToneGenerator.TONE_PROP_BEEP2,70)
        onBack()
    },contentAlignment=Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val p=pulse.value
            val rail=Path().apply {
                val cut=5.dp.toPx()
                moveTo(cut,1.dp.toPx());lineTo(size.width-cut,1.dp.toPx())
                lineTo(size.width-1.dp.toPx(),cut);lineTo(size.width-1.dp.toPx(),size.height-cut)
                lineTo(size.width-cut,size.height-1.dp.toPx());lineTo(cut,size.height-1.dp.toPx())
                lineTo(1.dp.toPx(),size.height-cut);lineTo(1.dp.toPx(),cut);close()
            }
            drawPath(rail,Color(0xFF071E2A))
            drawPath(rail,Color(0xFF66E6FA).copy(alpha=.38f+.62f*p),style=Stroke((.8f+p)*density))
            val y=if(bottom)size.height-3.dp.toPx() else 3.dp.toPx()
            drawLine(Color(0xFF9DF7FF).copy(alpha=.6f+.4f*p),androidx.compose.ui.geometry.Offset(8.dp.toPx(),y),
                androidx.compose.ui.geometry.Offset(size.width-8.dp.toPx(),y),(1f+3f*p)*density)
            drawCircle(Color(0xFF77ECFF).copy(alpha=.35f+.65f*p),radius=(2f+2f*p)*density,
                center=androidx.compose.ui.geometry.Offset(12.dp.toPx(),size.height/2))
        }
        Text("‹ BACK",color=Color(0xFFB7F5FF),fontWeight=FontWeight.Bold,fontSize=12.sp,
            modifier=Modifier.padding(start=7.dp))
    }
}

/** Routes supply navigation; the existing content housing owns its back key. */
internal val LocalPageBack=staticCompositionLocalOf<(() -> Unit)?> {null}

@Composable internal fun PageBackControl() {
    LocalPageBack.current?.let {onBack->
        // Reuse the frame artwork for a small inset housing in the content header.
        OpsTelemetryHousing(Modifier.width(84.dp).height(44.dp),compact=true) {
            Box(Modifier.align(Alignment.Center)) {FrameBackControl(compact=true,onBack=onBack)}
        }
    }
}

@Composable
internal fun SectionWindow(framed:Boolean,onBack:()->Unit,content:@Composable ()->Unit) {
    CompositionLocalProvider(LocalPageBack provides if(framed)onBack else null) {content()}
}
