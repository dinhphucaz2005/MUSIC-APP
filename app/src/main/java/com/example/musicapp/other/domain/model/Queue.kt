package com.example.musicapp.other.domain.model

import com.example.innertube.models.BrowseEndpoint
import com.example.innertube.models.SongItem
import com.example.musicapp.extension.toSong

sealed class Queue(
    open val id: String = "",
    open var index: Int = 0,
    open val songs: List<Any> = emptyList(),
) {

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
        val relatedEndpoint: BrowseEndpoint? = null,
    ) : Queue()

    companion object {
        const val FIREBASE_ID: String = "firebase"
        const val LOCAL_ID = "local"
    }

    class Builder {
        private var id = ""
        private var otherSongs: List<Song> = emptyList()
        private var youtubeSongs: List<SongItem> = emptyList()
        private val relatedEndpoint: BrowseEndpoint? = null
        private var index = 0

        fun setId(id: String) = apply { this.id = id }

        fun setOtherSongs(songs: List<Song>) = apply { otherSongs = songs }

        fun setYoutubeSongs(songs: List<SongItem>) = apply { youtubeSongs = songs }

        fun setRelatedEndpoint(relatedEndpoint: BrowseEndpoint) =
            apply { relatedEndpoint.let { this.relatedEndpoint } }

        fun setIndex(index: Int) = apply { this.index = index }

        fun build(): Queue {
            return when {
                otherSongs.isNotEmpty() -> Other(id, index, otherSongs)
                else -> Youtube(id, index, youtubeSongs)
            }
        }
    }
}