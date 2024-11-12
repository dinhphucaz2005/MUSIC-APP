package com.example.musicapp.domain.model

data class Queue(
    val id: String,
    val songs: List<Song>
) {
    class Builder {
        private var id: String = UNIDENTIFIED_ID
        private var songs: List<Song> = emptyList()

        fun setLocalSong(songs: List<Song>, localId: String? = null): Builder {
            this.songs = songs
            this.id = "$LOCAL_SONG_ID/$localId"
            return this
        }

        fun setFirebaseSong(songs: List<Song>, firebaseId: String? = null): Builder {
            this.songs = songs
            this.id = "$FIREBASE_ID/$firebaseId"
            return this
        }

        fun setYoutubeSong(songs: List<Song>, youtubeId: String? = null): Builder {
            this.songs = songs
            this.id = "$YOUTUBE_ID/$youtubeId"
            return this
        }

        fun setSavePlayListSong(songs: List<Song>, savePlayListId: String? = null): Builder {
            this.songs = songs
            this.id = "$SAVE_PLAYLIST_ID/$savePlayListId"
            return this
        }

        fun build(): Queue {
            return Queue(id, songs)
        }
    }

    companion object {
        const val UNIDENTIFIED_ID = "UNIDENTIFIED_ID"
        const val FIREBASE_ID = "FIREBASE_ID"
        const val YOUTUBE_ID = "YOUTUBE_ID"
        const val LOCAL_SONG_ID = "LOCAL_SONG_ID"
        private const val SAVE_PLAYLIST_ID = "SAVE_PLAYLIST_ID"
    }
}
