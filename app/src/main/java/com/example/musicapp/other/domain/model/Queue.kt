package com.example.musicapp.other.domain.model

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
    }
}

/*
usage:
    LOCAL_ID: Queue from local file
    YOUTUBE_SONG_ID: Queue from song of youtube (example: YOUTUBE_SONG_ID + "/" + youtubeSongId)
    YOUTUBE_PLAYLIST_ID: Queue from playlist of youtube(example: YOUTUBE_PLAYLIST_ID + "/" + playlistId)
 */