package com.example.player.model

data class Queue(
    val id: String = "",
    var index: Int = 0,
    val songs: List<Song> = emptyList(),
) {
    companion object {
        const val LOCAL_ID = "LOCAL_ID"
        const val FIREBASE_ID = "FIREBASE_ID"
        const val YOUTUBE_SONG_ID = "YOUTUBE_SONG_ID"
        const val YOUTUBE_PLAYLIST_ID = "YOUTUBE_PLAYLIST_ID"
        const val SAVED_PLAYLIST_ID = "SAVED_PLAYLIST_ID"
        const val LIKED_PLAYLIST_ID = "LIKED_PLAYLIST_ID"
    }
}
