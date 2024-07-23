package com.example.mymusicapp.data.dto

data class PlayList(
    val id: Int,
    val name: String = "Unnamed",
    val songs: MutableList<SongFileDTO> = mutableListOf()
) {
    fun addSong(value: SongFileDTO?) {
        if (value != null) {
            songs.add(value)
        }
    }
}