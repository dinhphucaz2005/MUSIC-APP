package com.example.musicapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val customDarkColorScheme = darkColorScheme(
    // Keep existing colors
    primary = Color(0xFF171C26),
    onPrimary = Color.White,
    secondary = Color(0xFF6C7689),
    onSecondary = Color.White,
    tertiary = Color(0xFFA4AAB7),
    onTertiary = Color.Black,
    background = Color(0xFF171C26),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2E2E2E),
    onSurfaceVariant = Color(0xFFCCCCCC),
    surfaceTint = Color(0xFF171C26),
    inverseSurface = Color.White,
    inverseOnSurface = Color(0xFF171C26),
    inversePrimary = Color(0xFF9BB3FF),
    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color.White,
    primaryContainer = Color(0xFF2C313D),
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF444B5A),
    onSecondaryContainer = Color.White,
    tertiaryContainer = Color(0xFFBEC3D1),
    onTertiaryContainer = Color.Black,
    outline = Color(0xFF5D6270),
    outlineVariant = Color(0xFF3D4250),
    scrim = Color.Black.copy(alpha = 0.5f)
)

@Composable
fun MyMusicAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = customDarkColorScheme,
        typography = Typography,
        content = content
    )
}