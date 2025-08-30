package nd.phuc.core.model

import androidx.compose.ui.graphics.ImageBitmap

sealed class ThumbnailSource {

    fun getThumbnailUrl(): String? = if (this is FromUrl) url else null

    data class FromUrl(val url: String?) : ThumbnailSource()
    data class FromBitmap(val imageBitmap: ImageBitmap?) : ThumbnailSource()
}