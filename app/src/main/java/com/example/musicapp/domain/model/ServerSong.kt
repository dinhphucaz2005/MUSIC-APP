package com.example.musicapp.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import java.util.UUID

@Stable
@Immutable
@Serializable
data class ServerSong(
    override val id: String = UUID.randomUUID().toString(),
    val title: String = "Unknown song",
    val songUri: String = "",
    val artist: String = "Unknown artist",
    val duration: Long? = null, // millis
    val thumbnailUri: String? = null,
) : Identifiable {
    constructor(id: String?, song: Song, songUri: String, imageUri: String?) : this(
        id = id ?: UUID.randomUUID().toString(),
        title = song.title,
        songUri = songUri,
        artist = song.author,
        duration = song.duration,
    )
}