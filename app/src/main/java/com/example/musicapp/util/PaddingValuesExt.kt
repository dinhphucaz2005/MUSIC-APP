package com.example.musicapp.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp

fun PaddingValues.copy(
    start: Dp,
    end: Dp,
    top: Dp,
    bottom: Dp
): PaddingValues {
    return PaddingValues(
        start = start,
        end = end,
        top = top,
        bottom = bottom
    )
}
