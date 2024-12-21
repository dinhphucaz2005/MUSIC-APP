package com.example.musicapp.other.domain.model

import com.example.innertube.models.SongItem
import com.example.musicapp.extension.toSong

sealed class Queue(
    open val id: String = "",
    open var index: Int = 0,
    open val songs: List<Any> = emptyList(),
) {
    fun getCurrentThumbnail(): ThumbnailSource {
        return when (this) {
            is Other -> (songs.getOrNull(index) ?: Song.unidentifiedSong()).thumbnailSource

            is Youtube -> ThumbnailSource.FromUrl(
                (songs.getOrNull(index) ?: SongItem.unidentifiedSong()).thumbnail
            )

        }
    }


    fun getSong(index: Int?): Song {
        if (index == null) return Song.unidentifiedSong()
        return when(this) {
            is Other -> songs.getOrNull(index) ?: Song.unidentifiedSong()
            is Youtube -> songs.getOrNull(index)?.toSong() ?: Song.unidentifiedSong()
        }
    }

    data class Other(
        override val id: String,
        override var index: Int,
        override val songs: List<Song>,
    ) : Queue()

    data class Youtube(
        override val id: String,
        override var index: Int,
        override val songs: List<SongItem>,
    ) : Queue()

    companion object {
        const val FIREBASE_ID: String = "firebase"
        const val LOCAL_ID = "local"
    }

    class Builder {
        private var id = ""
        private val otherSongs: MutableList<Song> = mutableListOf()
        private val youtubeSongs: MutableList<SongItem> = mutableListOf()
        private var index = 0

        fun setId(id: String) = apply { this.id = id }
        fun setOtherSongs(songs: List<Song>) = apply { otherSongs.addAll(songs) }
        fun setYoutubeSongs(songs: List<SongItem>) = apply { youtubeSongs.addAll(songs) }

        fun setIndex(index: Int) = apply { this.index = index }


        fun build(): Queue {
            return when {
                otherSongs.isNotEmpty() -> Other(id, index, otherSongs)
                else -> Youtube(id, index, youtubeSongs)
            }
        }
    }
}