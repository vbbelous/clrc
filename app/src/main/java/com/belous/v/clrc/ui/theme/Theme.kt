package com.belous.v.clrc.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun CLRCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkAppColors else lightAppColors

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides appTypography,
        LocalAppDimensions provides appDimensions,
        content = content
    )
}