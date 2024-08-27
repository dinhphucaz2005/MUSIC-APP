package com.example.musicapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun CommonImage(
    bitmap: ImageBitmap?,
    painter: Painter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (bitmap == null)
        Image(
            painter = painter,
            modifier = modifier,
            contentDescription = null,
            contentScale = contentScale
        )
    else
        Image(
            bitmap = bitmap,
            modifier = modifier,
            contentDescription = null,
            contentScale = contentScale
        )
}