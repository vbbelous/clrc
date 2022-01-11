package com.belous.v.clrc.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class AppColors(
    val mainBg: Color,
    val itemBg: Color,
    val dividerBg: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val green: Color = Color(0xFF1B8A20),
    val red: Color = Color(0xFFFF0000),
    val orange: Color = Color(0xFFE5A65B)
)

data class AppTypography(
    val smallNormal: TextStyle,
    val smallBold: TextStyle,
    val mediumNormal: TextStyle,
    val mediumSemiBold: TextStyle,
    val mediumBold: TextStyle,
    val largeNormal: TextStyle,
    val largeSemiBold: TextStyle,
    val largeBold: TextStyle,
    val xLargeNormal: TextStyle,
    val xLargeBold: TextStyle,
    val xxLargeNormal: TextStyle,
    val xxLargeBold: TextStyle,
)

data class AppDimensions(
    val divider: Dp,
    val xSmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xLarge: Dp,
    val xxLarge: Dp,
    val itemHeight: Dp
)

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No colors provider")
}

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No font provided")
}

val LocalAppDimensions = staticCompositionLocalOf<AppDimensions> {
    error("No dimensions provided")
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

    val dimensions: AppDimensions
        @Composable
        get() = LocalAppDimensions.current
}