package com.example.musicapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R


val LatoFont = FontFamily(
    Font(R.font.lato_black, FontWeight.Black),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_medium, FontWeight.Medium),
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_semibold, FontWeight.SemiBold),
    Font(R.font.lato_thin, FontWeight.Thin),
)


val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = LatoFont,
        fontWeight = FontWeight.Black,
        fontSize = 12.sp
    )
)

val commonShape = RoundedCornerShape(12.dp)