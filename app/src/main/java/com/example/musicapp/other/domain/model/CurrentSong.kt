package com.example.musicapp.other.domain.model

import com.example.innertube.CustomYoutube
import com.example.innertube.models.SongItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

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

    fun getDownloadUrl(): String = runBlocking(Dispatchers.IO) {

        val result = "NO URL"
        if (this@CurrentSong is OtherSong) {
            return@runBlocking if (song.audioSource is AudioSource.FromUrl)
                song.audioSource.url
            else
                result
        } else if (this@CurrentSong is YoutubeSong) {
            val youtubeMediaId = song.id
            CustomYoutube.player(youtubeMediaId)
                .onSuccess { response ->
                    return@runBlocking response
                        .streamingData
                        ?.adaptiveFormats
                        ?.filter { it.isAudio }
                        ?.maxByOrNull {
                            it.bitrate + (if (it.mimeType.startsWith("audio/webm")) 10240 else 0)
                        }?.url.toString()
                }.onFailure {
                    return@runBlocking result
                }
        }
        return@runBlocking result
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