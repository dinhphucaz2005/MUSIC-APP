package com.example.musicapp.core.presentation.theme

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
fun MusicTheme(
    myColorScheme: ColorScheme = MyColorScheme,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = myColorScheme,
        typography = Typography,
        content = content
    )
}
val SandYellow = Color(0xFFFFBD64)
val SearchBarBackground = Color(0xFF000000)
val OnSecondary = Color(0xFFFFFFFF)
val White = Color(0xFFffffff)
val Black = Color(0xFF000000)
val DarkGray = Color(0xFF1d1d1d)
val LightGray = Color(0xFFababab)
val Primary = Color(0xFFe15249)