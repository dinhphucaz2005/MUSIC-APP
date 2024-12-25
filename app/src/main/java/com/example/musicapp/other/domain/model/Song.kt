package com.example.musicapp.other.domain.model

import android.annotation.SuppressLint
import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.innertube.models.Artist
import com.example.innertube.models.SongItem
import com.example.musicapp.extension.toArtistString
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.other.data.database.entity.LikedSong
import java.util.UUID

abstract class Song(
    open val id: String = UUID.randomUUID().toString()
) {

    abstract fun getSongTitle(): String

    abstract fun getSongArtist(): String

    abstract fun getThumbnail(): ThumbnailSource

    abstract fun getDuration(): String

    abstract fun toMediaItem(): MediaItem

    abstract fun getArtistId(): String?

    abstract fun toLikedSong(): LikedSong

    companion object {
        fun unidentifiedSong(id: String = UUID.randomUUID().toString()): Song {
            return object : Song(id) {
                override fun getArtistId(): String? {
                    return null
                }

                override fun toLikedSong(): LikedSong {
                    return LikedSong(
                        audioSource = "",
                        thumbnail = "",
                        type = LikedSong.UNKNOWN
                    )
                }

                override fun getSongTitle(): String = "No song is playing"

                override fun getSongArtist(): String = "No artist"

                override fun getThumbnail(): ThumbnailSource = ThumbnailSource.FromBitmap(null)

                override fun getDuration(): String = "00:00"

                override fun toMediaItem(): MediaItem {
                    return MediaItem.Builder().apply {
                        setUri(Uri.EMPTY)
                        setMediaMetadata(
                            MediaMetadata.Builder().apply {
                                setTitle("No song is playing")
                                setArtist("No artist")
                            }.build()
                        )
                    }.build()
                }
            }
        }
    }

}

data class LocalSong(
    override val id: String,
    val title: String,
    val artist: String,
    val audio: Uri,
    val thumbnailSource: ThumbnailSource,
    val durationMillis: Long?
) : Song() {

    override fun getSongTitle(): String = this.title

    override fun getSongArtist(): String = this.artist

    override fun getThumbnail(): ThumbnailSource = this.thumbnailSource

    override fun getDuration(): String = this.durationMillis.toDurationString()

    override fun toMediaItem(): MediaItem = MediaItem.Builder().apply {
        setUri(audio)
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle(title)
                setArtist(artist)
                when (thumbnailSource) {
                    is ThumbnailSource.FromUrl -> setArtworkUri(thumbnailSource.url?.toUri())
                    else -> {
                        // Not to do anything
                    }
                }
            }.build()
        )
    }.build()

    override fun getArtistId(): String? {
        return null
    }

    override fun toLikedSong(): LikedSong = LikedSong(
        audioSource = audio.path ?: "",
        type = LikedSong.LOCAL
    )

}

data class FirebaseSong(
    override val id: String,
    val title: String,
    val artist: String,
    val audioUrl: String,
    val thumbnailSource: ThumbnailSource,
    val durationMillis: Long?
) : Song() {

    override fun getSongTitle(): String = this.title

    override fun getSongArtist(): String = this.artist

    override fun getThumbnail(): ThumbnailSource = this.thumbnailSource

    override fun getDuration(): String = this.durationMillis.toDurationString()

    override fun toMediaItem(): MediaItem = MediaItem.Builder().apply {
        setUri(audioUrl.toUri())
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle(title)
                setArtist(artist)
                when (thumbnailSource) {
                    is ThumbnailSource.FromUrl -> setArtworkUri(thumbnailSource.url?.toUri())
                    else -> {
                        // Not to do anything
                    }
                }
            }.build()
        )
    }.build()

    override fun getArtistId(): String? {
        return null
    }

    override fun toLikedSong(): LikedSong = LikedSong(
        audioSource = audioUrl,
        thumbnail = if (thumbnailSource is ThumbnailSource.FromUrl) thumbnailSource.url else null,
        type = LikedSong.FIREBASE
    )

}

data class YoutubeSong(
    override val id: String,
    val title: String,
    val artists: List<Artist>,
    val thumbnail: String,
    val durationMillis: Long?
) : Song() {

    constructor(songItem: SongItem) : this(
        id = songItem.id,
        title = songItem.title,
        artists = songItem.artists,
        thumbnail = songItem.thumbnail,
        durationMillis = songItem.duration?.times(1000L)
    )

    override fun getSongTitle(): String = this.title

    override fun getSongArtist(): String = this.artists.toArtistString()

    override fun getThumbnail(): ThumbnailSource = ThumbnailSource.FromUrl(this.thumbnail)

    override fun getDuration(): String = this.durationMillis.toDurationString()

    @SuppressLint("UnsafeOptInUsageError")
    override fun toMediaItem(): MediaItem = MediaItem.Builder().apply {
        /**
         * Set the song ID (YouTube Song item) as the custom cache key.
         * When MusicService needs to retrieve song information,
         * it will use this customCacheKey to call the YouTube API with the song's ID.
         * - MusicService will use the CustomMediaSourceFactory with this customCacheKey (ID).
         * - [com.example.musicapp.service.CustomMediaSourceFactory.createDataSourceFactory] will use the ID to call the YouTube API and get the song's URL.
         * - This URL can then be used to play the song from YouTube.
         *
         * Note: When the API is called, it will return the URL or song data from YouTube,
         * and CustomMediaSource will handle the request accordingly.
         *
         * @see [com.example.musicapp.service.CustomMediaSourceFactory.createDataSourceFactory] Go to the function that retrieves the song URL using the provided ID.
         */
        setCustomCacheKey(id)
        setUri(id)
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle(getSongTitle())
                setArtist(getSongArtist())
                setArtworkUri(thumbnail.toUri())
            }.build()
        )
    }.build()

    override fun getArtistId(): String? {
        return this.artists.firstOrNull()?.id
    }

    override fun toLikedSong(): LikedSong = LikedSong(
        audioSource = id,
        thumbnail = thumbnail,
        type = LikedSong.YOUTUBE
    )

}