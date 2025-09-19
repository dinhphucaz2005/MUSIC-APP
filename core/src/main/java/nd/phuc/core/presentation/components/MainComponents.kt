package nd.phuc.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import nd.phuc.core.domain.model.ThumbnailSource
import nd.phuc.core.presentation.theme.Black

@Composable
fun Thumbnail(
    modifier: Modifier = Modifier,
    thumbnailSource: ThumbnailSource?,
    contentScale: ContentScale = ContentScale.Fit,
) {

    if (thumbnailSource == null) {
        throw Exception("Thumbnail source is null")
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

            else -> {
                Box(
                    modifier = modifier
                        .background(Black)
                ) {
                    Text(this::javaClass.name)
                }
            }
        }
}


@Composable
fun Thumbnail(
    modifier: Modifier = Modifier, thumbnail: ImageBitmap?,
    contentScale: ContentScale = ContentScale.Fit,
) {
    thumbnail?.let {
        Image(
            bitmap = it,
            contentScale = contentScale,
            contentDescription = null,
            modifier = modifier
        )
    } ?: Box(
        modifier = modifier
            .background(Black)
    ) {
    }
}