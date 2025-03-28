package com.example.musicapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

const val backgroundTemp = 0xFF1a2730
val MyColorScheme = lightColorScheme().copy(
    background = Color(0xFF0d1412),
    primary = Color(0xFFdbe2e0),
    secondary = Color(0xFF46586c),
    onSecondary = Color(0xFFafcee2),
    error = Color(0xFFFF5252),
    onError = Color(0xFF1f1f1f),
    tertiary = Color(0xFF74c6c6),
    onTertiary = Color(0xFF1d2a34)
)

@Composable
fun MyMusicAppTheme(
    myColorScheme: ColorScheme = MyColorScheme,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = myColorScheme,
        typography = Typography,
        content = content
    )
}
