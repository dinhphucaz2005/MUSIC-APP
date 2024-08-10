package com.example.mymusicapp.domain.model

data class Playlist(
    val id: Long = 0,
    val name: String = "Unnamed",
    val songs: MutableList<Song> = mutableListOf(),
)