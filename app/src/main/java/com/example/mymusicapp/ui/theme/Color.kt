package com.example.mymusicapp.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val Background = Color(0xFFe0f5ec)
val IconTintColor = Color(0xFF2f604b)
val TextColor = Color(0xFF30614c)
val MyBrush = Brush.linearGradient(
    listOf(
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)