package com.example.mymusicapp.domain.model

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap

data class Song(
    val fileName: String = "",
    val uri: Uri? = null,
    val path: String? = null,
    val title: String? = null,
    val artist: String? = null,
    val imageBitmap: ImageBitmap? = null,
    val smallBitmap: ImageBitmap? = null,
    val duration: Long? = null
) {
    operator fun times(i: Int): List<Song> {
        return List(i) { this }
    }
}


