package com.example.musicapp.domain.model

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap

data class Song(
    var id: Long? = 0,
    val fileName: String = "", // File name with out extension and path: "file_name"
    val uri: Uri? = null,
    val path: String? = null, // File path with extension: "/storage/emulated/0/file_name.mp3"
    val title: String? = null,
    val author: String? = null,
    val smallBitmap: ImageBitmap? = null,
    val thumbnail: ImageBitmap? = null,
    val duration: Long? = null
) {
    operator fun times(i: Int): List<Song> {
        return List(i) { this }
    }
}