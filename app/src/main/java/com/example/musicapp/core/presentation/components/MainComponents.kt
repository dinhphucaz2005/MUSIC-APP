package com.example.musicapp.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.example.musicapp.R
import com.example.musicapp.music.domain.model.ThumbnailSource

@Composable
fun Thumbnail(
    modifier: Modifier = Modifier,
    thumbnailSource: ThumbnailSource?,
    contentScale: ContentScale = ContentScale.Fit
) {

    if (thumbnailSource == null) {
        Image(
            painter = painterResource(R.drawable.image),
            contentScale = contentScale,
            contentDescription = null,
            modifier = modifier
        )
    } else
        when (thumbnailSource) {
            is ThumbnailSource.FromBitmap -> {
                Thumbnail(
                    modifier = modifier,
                    thumbnail = thumbnailSource.imageBitmap,
                    contentScale = contentScale
                )
            }

            is ThumbnailSource.FromUrl -> {
                AsyncImage(
                    model = thumbnailSource.url,
                    contentDescription = null,
                    modifier = modifier,
                    contentScale = contentScale
                )
            }
        }
}


@Composable
fun Thumbnail(
    modifier: Modifier = Modifier, thumbnail: ImageBitmap?,
    contentScale: ContentScale = ContentScale.Fit
) {
    thumbnail?.let {
        Image(
            bitmap = it,
            contentScale = contentScale,
            contentDescription = null,
            modifier = modifier
        )
    } ?: Image(
        painter = painterResource(R.drawable.image),
        contentScale = contentScale,
        contentDescription = null,
        modifier = modifier
    )
}