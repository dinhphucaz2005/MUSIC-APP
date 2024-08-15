package com.example.mymusicapp.domain.model

import com.example.mymusicapp.data.database.entity.PlaylistEntity

data class Playlist(
    val id: Long = 0,
    val name: String = "Unnamed",
    val songs: MutableList<Song> = mutableListOf(),
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