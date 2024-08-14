package com.example.mymusicapp.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val MyBrush = Brush.linearGradient(
    listOf(
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29),
        Color(0xFF3D3D3C),
        Color(0xFF2B2A29)
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)

const val myBackgroundColor: Long = 0xFF2f604b
const val myTextColor: Long = 0xFFc0ddfd
const val tempColor: Long = 0xFFc0ddfd