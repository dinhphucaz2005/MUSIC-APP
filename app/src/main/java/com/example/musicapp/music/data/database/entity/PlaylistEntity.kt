package com.example.musicapp.music.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey val id: Int? = null,
    val name: String,
) {
    companion object {
        const val LIKED_PLAYLIST_ID = 0
    }
}