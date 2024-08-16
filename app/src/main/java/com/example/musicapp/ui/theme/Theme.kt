package com.example.musicapp.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.musicapp.R
import com.example.musicapp.extension.myFormat

const val backgroundTemp = 0xFF2b324c
val MyColorScheme = lightColorScheme().copy(
    background = Color(0xFF1a2730),
    primary = Color(0xFFafcee2),

    secondary = Color(0xFF46586c),
    onSecondary = Color(0xFFafcee2),
    error = Color(0xFFFF5252),
    onError = Color(0xFF1f1f1f),
    tertiary = Color(0xFFafcee2),
    onTertiary = Color(0xFF1d2a34)
)

@Composable
fun MusicTheme(
    context: Context = LocalContext.current,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    myColorScheme: ColorScheme = MyColorScheme,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ->
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)

        else -> MyColorScheme
    }
    println(colorScheme.myFormat())
    MaterialTheme(
        colorScheme = myColorScheme,
        typography = Typography,
        content = content
    )
}