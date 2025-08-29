package com.example.musicapp.music.domain.model

@Deprecated("No longer used, replaced by QueueItem")
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

/*
usage:
    LOCAL_ID: Queue from local file
    YOUTUBE_SONG_ID: Queue from song of youtube (example: YOUTUBE_SONG_ID + "/" + youtubeSongId)
    YOUTUBE_PLAYLIST_ID: Queue from playlist of youtube(example: YOUTUBE_PLAYLIST_ID + "/" + playlistId)
    SAVED_PLAYLIST_ID: Queue from playlist of app(example: SAVED_PLAYLIST_ID + "/" + playlistId)
 */