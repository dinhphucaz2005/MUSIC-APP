package com.example.musicapp.data.database.dto

data class PlaylistDTO(
    val id: Long,
    val name: String,
    val songId: Long?,
    val songTitle: String?,
    val songPath: String?
)