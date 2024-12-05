package com.example.musicapp.extension

import android.annotation.SuppressLint
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.innertube.models.SongItem
import com.example.musicapp.other.domain.model.AudioSource
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.ThumbnailSource

@SuppressLint("UnsafeOptInUsageError")
fun Song.toMediaItem(): MediaItem = MediaItem.Builder().apply {
    when (audioSource) {
        is AudioSource.FromLocalFile -> setUri(audioSource.uri)
        is AudioSource.FromUrl -> setUri(audioSource.url)
    }
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


@SuppressLint("UnsafeOptInUsageError")
fun SongItem.toMediaItemFromYT(): MediaItem = MediaItem.Builder().apply {
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
            setTitle(title)
            setArtist(artists.joinToString { it.name + "," })
            setArtworkUri(thumbnail.toUri())
        }.build()
    )
}.build()
