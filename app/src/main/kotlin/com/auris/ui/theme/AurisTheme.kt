package com.auris.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AurisLightColorScheme = lightColorScheme(
    primary          = AurisColors.Blue,
    onPrimary        = AurisColors.BgSecondary,
    primaryContainer = AurisColors.FillTertiary,
    secondary        = AurisColors.Green,
    onSecondary      = AurisColors.BgSecondary,
    background       = AurisColors.BgPrimary,
    surface          = AurisColors.BgSecondary,
    onBackground     = AurisColors.LabelPrimary,
    onSurface        = AurisColors.LabelPrimary,
    surfaceVariant   = AurisColors.FillTertiary,
    outline          = AurisColors.Separator,
    error            = AurisColors.Red
)

/**
 * AurisTheme — Apple iOS 17 light-mode Material3 wrapper.
 */
@Composable
fun AurisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AurisLightColorScheme,
        typography  = AurisTypography,
        content     = content
    )
}
