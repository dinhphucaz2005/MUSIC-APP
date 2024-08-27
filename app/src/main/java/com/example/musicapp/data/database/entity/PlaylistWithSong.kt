package com.example.musicapp.data.database.entity

data class PlaylistWithSong(
    val id: Long,
    val name: String,
    val songId: Long?,
    val songTitle: String?,
    val songPath: String?
)