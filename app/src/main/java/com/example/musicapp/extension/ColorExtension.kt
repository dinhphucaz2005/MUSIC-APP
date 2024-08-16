package com.example.musicapp.extension

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

fun Color.toHexadecimalString(): String {
    val red = (this.red * 255).toInt().toString(16).padStart(2, '0')
    val green = (this.green * 255).toInt().toString(16).padStart(2, '0')
    val blue = (this.blue * 255).toInt().toString(16).padStart(2, '0')
    return "Color(0xFF$red$green$blue)"
}

fun ColorScheme.myFormat(): String {
    return "ColorScheme(" +
            "primary= ${primary.toHexadecimalString()}," +
            "onPrimary= ${onPrimary.toHexadecimalString()}," +
            "primaryContainer= ${primaryContainer.toHexadecimalString()}," +
            "onPrimaryContainer= ${onPrimaryContainer.toHexadecimalString()}," +
            "inversePrimary= ${inversePrimary.toHexadecimalString()}," +
            "secondary= ${secondary.toHexadecimalString()}," +
            "onSecondary= ${onSecondary.toHexadecimalString()}," +
            "secondaryContainer= ${secondaryContainer.toHexadecimalString()}," +
            "onSecondaryContainer= ${onSecondaryContainer.toHexadecimalString()}," +
            "tertiary= ${tertiary.toHexadecimalString()}," +
            "onTertiary= ${onTertiary.toHexadecimalString()}," +
            "tertiaryContainer= ${tertiaryContainer.toHexadecimalString()}," +
            "onTertiaryContainer= ${onTertiaryContainer.toHexadecimalString()}," +
            "background= ${background.toHexadecimalString()}," +
            "onBackground= ${onBackground.toHexadecimalString()}," +
            "surface= ${surface.toHexadecimalString()}," +
            "onSurface= ${onSurface.toHexadecimalString()}," +
            "surfaceVariant= ${surfaceVariant.toHexadecimalString()}," +
            "onSurfaceVariant= ${onSurfaceVariant.toHexadecimalString()}," +
            "surfaceTint= ${surfaceTint.toHexadecimalString()}," +
            "inverseSurface= ${inverseSurface.toHexadecimalString()}," +
            "inverseOnSurface= ${inverseOnSurface.toHexadecimalString()}," +
            "error= ${error.toHexadecimalString()}," +
            "onError= ${onError.toHexadecimalString()}," +
            "errorContainer= ${errorContainer.toHexadecimalString()}," +
            "onErrorContainer= ${onErrorContainer.toHexadecimalString()}," +
            "outline= ${outline.toHexadecimalString()}," +
            "outlineVariant= ${outlineVariant.toHexadecimalString()}," +
            "scrim= ${scrim.toHexadecimalString()}," +
            "surfaceBright= ${surfaceBright.toHexadecimalString()}," +
            "surfaceDim= ${surfaceDim.toHexadecimalString()}," +
            "surfaceContainer= ${surfaceContainer.toHexadecimalString()}," +
            "surfaceContainerHigh= ${surfaceContainerHigh.toHexadecimalString()}," +
            "surfaceContainerHighest= ${surfaceContainerHighest.toHexadecimalString()}," +
            "surfaceContainerLow= ${surfaceContainerLow.toHexadecimalString()}," +
            "surfaceContainerLowest= ${surfaceContainerLowest.toHexadecimalString()}," +
            ")"
}

fun Color.newColor(factor: Float): Color {
    val red = (this.red * factor).coerceIn(0f, 1f) * 255
    val green = (this.green * factor).coerceIn(0f, 1f) * 255
    val blue = (this.blue * factor).coerceIn(0f, 1f) * 255
    return Color(red.toInt(), green.toInt(), blue.toInt())
}