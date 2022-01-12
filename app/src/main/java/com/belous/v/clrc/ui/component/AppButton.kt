package com.belous.v.clrc.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.belous.v.clrc.ui.theme.AppTheme

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(end = AppTheme.dimensions.medium),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.itemBg,
            disabledBackgroundColor = AppTheme.colors.itemBg,
            contentColor = AppTheme.colors.primaryText,
            disabledContentColor = AppTheme.colors.disabledText
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = -AppTheme.dimensions.small
        ),
        enabled = enabled,
        onClick = { onClick() }) {
        Text(
            text = text,
            style = AppTheme.typography.mediumNormal
        )
    }
}