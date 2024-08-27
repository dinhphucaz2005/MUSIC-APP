package com.example.musicapp.domain.model

import com.example.musicapp.data.database.entity.PlaylistEntity

data class Playlist(
    val id: Long = 0,
    var name: String = "Unnamed",
    val songs: MutableList<Song> = mutableListOf(),
    var currentSong: Int? = null
) {
    companion object {
        fun create(it: PlaylistEntity): Playlist {
            return Playlist(
                it.id,
                it.name,
                mutableListOf()
            )
        }
    }
}