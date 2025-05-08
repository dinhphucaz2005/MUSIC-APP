package com.example.musicapp.music.domain.model

import androidx.compose.runtime.Immutable


@Immutable
data class Playlist(
    val id: Int,
    val name: String,
    val songs: List<Song> = emptyList()
) {

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (id != other.id) return false
        if (name != other.name) return false
        if (songs != other.songs) return false

        return true
    }
}