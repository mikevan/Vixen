package com.projectrevivesolutions.layertime.controller.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Routes supply navigation; the existing content housing owns its back key. */
internal val LocalPageBack=staticCompositionLocalOf<(() -> Unit)?> {null}

@Composable internal fun PageBackControl() {
    if(LocalPageBack.current!=null)Spacer(Modifier.width(96.dp).height(24.dp))
}

@Composable
internal fun SectionWindow(framed:Boolean,onBack:()->Unit,content:@Composable ()->Unit) {
    CompositionLocalProvider(LocalPageBack provides if(framed)onBack else null) {content()}
}

/** One content housing with the same border-owned Back for analytic pages. */
@Composable internal fun BackContentFrame(onBack:()->Unit,headerSlot:Boolean=true,content:@Composable ()->Unit) {
    CompositionLocalProvider(LocalPageBack provides onBack) {
        OpsTelemetryHousing(Modifier.fillMaxSize(),borderBack=onBack) {
            Box(Modifier.fillMaxSize().padding(start=18.dp,end=18.dp,top=if(headerSlot)20.dp else 50.dp,bottom=24.dp)){content()}
        }
    }
}
