package com.example.musicapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.musicapp.R

@Composable
fun Thumbnail(
    modifier: Modifier = Modifier, bitmap: ImageBitmap?
) {
    bitmap?.let {
        Image(
            bitmap = it,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = modifier
        )
    } ?: Image(
        painter = painterResource(R.drawable.image),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
    )
}



