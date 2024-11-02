package com.example.musicapp.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap

@Immutable
data class Song(
    val id: Long,
    val uri: Uri,
    val title: String,
    val author: String,
    val smallBitmap: ImageBitmap? = null,
    val duration: Long? = null
) {
    companion object {
        private const val INVALID_NUMBER = 0L // song unavailable
    }

    constructor() : this(
        INVALID_NUMBER,
        Uri.EMPTY,
        "",
        "",
        null,
        null
    )


    operator fun times(i: Int): List<Song> {
        return List(i) { this }
    }

    fun getFileName() = uri.lastPathSegment?.substringAfterLast("/")

    fun getPath(): String? = uri.path
}
