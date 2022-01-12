package com.belous.v.clrc.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.belous.v.clrc.ui.theme.AppTheme

@Composable
fun EventSnackbar(snackbarData: SnackbarData) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .focusable(false)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                snackbarData.dismiss()
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Snackbar(
            snackbarData = snackbarData,
            backgroundColor = AppTheme.colors.itemBg,
            contentColor = AppTheme.colors.primaryText,
            actionColor = AppTheme.colors.orange,
            modifier = Modifier
                .clickable { snackbarData.dismiss() }
        )
    }
}