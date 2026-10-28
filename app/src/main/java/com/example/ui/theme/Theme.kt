package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CelestialColorScheme = darkColorScheme(
    primary = StarlightGold,
    onPrimary = Color(0xFF1E1500),
    primaryContainer = StarlightGoldDark,
    onPrimaryContainer = Color(0xFFFFFAEB),
    secondary = SacredCyan,
    onSecondary = Color(0xFF003548),
    secondaryContainer = Color(0xFF004D68),
    onSecondaryContainer = Color(0xFFC2E8FF),
    tertiary = MysticPurple,
    onTertiary = Color(0xFF381E72),
    background = CelestialNavy,
    onBackground = CosmicTextPrimary,
    surface = CosmicSurface,
    onSurface = CosmicTextPrimary,
    surfaceVariant = CosmicSurfaceVariant,
    onSurfaceVariant = CosmicTextSecondary,
    outline = Color(0xFF334155)
)

@Composable
fun JyotishVedaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CelestialColorScheme,
        typography = Typography,
        content = content
    )
}
