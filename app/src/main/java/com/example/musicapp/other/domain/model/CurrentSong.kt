package com.example.musicapp.other.domain.model

import com.example.innertube.models.SongItem

sealed class CurrentSong {
    fun getTitle(): String {
        return when (this) {
            is OtherSong -> song.title
            is YoutubeSong -> song.title
        }
    }

    fun getArtistName(): String {
        return when (this) {
            is OtherSong -> song.artist
            is YoutubeSong -> song.artists.joinToString { it.name }
        }
    }

    fun getArtistId(): String? {
        return when (this) {
            is OtherSong -> null
            is YoutubeSong -> song.artists.firstOrNull()?.id
        }
    }

    fun getThumbnail(): ThumbnailSource {
        return when (this) {
            is OtherSong -> song.thumbnailSource
            is YoutubeSong -> ThumbnailSource.FromUrl(song.thumbnail)
        }
    }

    fun getDurationMillis(): Long? {
        return when (this) {
            is OtherSong -> song.durationMillis
            is YoutubeSong -> song.duration?.toLong()?.times(1000)
        }
    }

    data class OtherSong(
        val song: Song
    ) : CurrentSong()

    data class YoutubeSong(
        val song: SongItem
    ) : CurrentSong()

    companion object {
        fun unidentifiedSong() = OtherSong(Song.unidentifiedSong())
    }
}