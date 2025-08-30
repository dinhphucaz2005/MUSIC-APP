package nd.phuc.core.extension

import android.media.MediaMetadataRetriever
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun MediaMetadataRetriever.getImageBitmap(): ImageBitmap? = embeddedPicture
    ?.toBitmap()?.asImageBitmap()

fun MediaMetadataRetriever.getDuration(): Long =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L

fun MediaMetadataRetriever.getTitle(): String? =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

fun MediaMetadataRetriever.getAuthor(): String =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown"
