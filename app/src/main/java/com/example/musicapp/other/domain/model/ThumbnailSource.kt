package com.example.musicapp.other.domain.model

import androidx.compose.ui.graphics.ImageBitmap

sealed class ThumbnailSource {
    data class FromUrl(val url: String?) : ThumbnailSource()
    data class FromBitmap(val imageBitmap: ImageBitmap?) : ThumbnailSource()
}