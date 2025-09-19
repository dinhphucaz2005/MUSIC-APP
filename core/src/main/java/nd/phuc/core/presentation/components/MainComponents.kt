package nd.phuc.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
                thumbnailSource.imageBitmap?.let {
                    Image(
                        bitmap = it,
                        contentScale = contentScale,
                        contentDescription = null,
                        modifier = modifier
                    )
                } ?: Box(
                    modifier = modifier
                        .background(Black)
                        .border(1.dp, White, shape = RoundedCornerShape(12.dp))
                ) {

                }
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