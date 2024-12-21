package com.example.musicapp.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.musicapp.R


private val RobotoFont = FontFamily(
    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_thin, FontWeight.Thin),
)


val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RobotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)