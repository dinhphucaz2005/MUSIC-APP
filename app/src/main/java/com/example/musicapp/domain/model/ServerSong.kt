package com.example.musicapp.domain.model

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class ServerSong(
    val id: String,
    val title: String,
    val songUri: String,
    val artist: String = "Unknown artist",
    val duration: Long? = null, // millis
    val thumbnailUri: String? = null,
) {
    constructor() : this(
        UUID.randomUUID().toString(),
        "No title",
        "NO uri",
        "Unknown artist",
        null,
        null
    )

    constructor(song: Song, songUri: String, thumbnailUri: String?) : this(
        UUID.randomUUID().toString(),
        song.title,
        songUri,
        song.author,
        song.duration,
        thumbnailUri
    )
}