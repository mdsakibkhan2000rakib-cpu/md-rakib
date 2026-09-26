package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GamingColorScheme = darkColorScheme(
    primary = CyberOrange,
    onPrimary = CyberBgDark,
    primaryContainer = CyberOrangeDark,
    onPrimaryContainer = CyberOrangeLight,
    secondary = CyberCyan,
    onSecondary = CyberBgDark,
    secondaryContainer = CyberSurfaceDark,
    onSecondaryContainer = CyberCyanLight,
    tertiary = CyberGreen,
    onTertiary = CyberBgDark,
    background = CyberBgDark,
    onBackground = TextPrimary,
    surface = CyberSurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = CyberCardDark,
    onSurfaceVariant = TextSecondary,
    outline = CyberCardBorder
)

@Composable
fun RakibGameLauncherTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GamingColorScheme,
        typography = Typography,
        content = content
    )
}
