package com.example.musicapp.domain.model

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.media3.common.MediaMetadata
import com.example.musicapp.extension.toBitmap

data class Song(
    var id: Long? = null,
    val uri: Uri? = null,
    val fileName: String, // File name with out extension and path: "file_name"
    val path: String, // File path with extension: "/storage/emulated/0/file_name.mp3"
    val title: String,
    val author: String,
    val smallBitmap: ImageBitmap? = null,
    val duration: Long? = null
) {

    constructor() : this(null, null, "No file name", "No path", "No title", "No author")

    constructor(mediaMetadata: MediaMetadata) : this(
        null,
        null,
        mediaMetadata.title.toString(),
        mediaMetadata.title.toString(),
        mediaMetadata.title.toString(),
        mediaMetadata.artist.toString(),
        mediaMetadata.artworkData?.toBitmap()?.asImageBitmap(),
    )

    operator fun times(i: Int): List<Song> {
        return List(i) { this }
    }
}
