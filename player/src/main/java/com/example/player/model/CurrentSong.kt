package com.example.player.model

data class CurrentSong(
    val data: Song,
    val isLiked: Boolean = false,
) {
    companion object {
        fun unidentifiedSong(): CurrentSong {
            return CurrentSong(Song.unidentifiedSong())
        }
    }
}