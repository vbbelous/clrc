package com.belous.v.clrc.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.belous.v.clrc.ui.theme.AppTheme

@Composable
fun ProgressIndicator(
    state: State<Boolean>
) {
    if (state.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.itemBg.copy(alpha = 0.33f))
                .focusable(true)
                .selectable(selected = true, enabled = false) {},
            contentAlignment = Alignment.TopCenter
        ) {
            LinearProgressIndicator(
                color = AppTheme.colors.orange,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
