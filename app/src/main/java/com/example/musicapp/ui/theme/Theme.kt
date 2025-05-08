package com.example.musicapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Dark theme colors - improved palette
val primary = Color(0xFFFFFFFF)         // Rich purple
val secondary = Color(0xFF7B2CBF)       // Deep purple
val tertiary = Color(0xFFC77DFF)        // Light purple
val background = Color(0xFF10002B)      // Deep purple-black
val surface = Color(0xFF240046)         // Rich dark purple

// Text and icon colors
val white = Color(0xFFF8F9FA)
val lightGray = Color(0xFFE2E2E2)
val darkGray = Color(0xFF2A2A2A)
val black = Color(0xFF000000)

// Custom color scheme
private val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondary,
    tertiary = tertiary,
    background = background,
    surface = surface,
    onPrimary = white,
    onSecondary = white,
    onTertiary = black,
    onBackground = white,
    onSurface = white
)

// Theme extension to provide custom brushes
data class AppBrushes(
    val playerGradient: Brush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFD5ADEF),
            Color(0xFFBFA2DB),
        )
    ),
    val songItemGradient: Brush = Brush.radialGradient(
        colors = listOf(Color(0xFFE0BBE4), Color(0xFFD291BC), Color(0xFF957DAD), Color(0xFFB5AEDF)),
        center = Offset.Infinite,
        radius = 1000f
    )
)

val LocalAppBrushes = staticCompositionLocalOf { AppBrushes() }

@Composable
fun MyMusicAppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppBrushes provides AppBrushes()
    ) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}