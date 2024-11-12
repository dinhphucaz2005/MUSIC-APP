package com.example.musicapp.domain.model

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.data.database.entity.SongEntity
import com.example.musicapp.extension.toBitmap
import com.example.musicapp.extension.toByteArray
import java.util.UUID

@Immutable
data class Song(
    override val id: String,
    val title: String,
    val artist: String,
    val audioSource: AudioSource,
    val thumbnailSource: ThumbnailSource,
    val durationMillis: Long
) : Identifiable {
    companion object {
        private const val UNIDENTIFIED_ID = "unidentified"
        fun unidentifiedSong(): Song {
            return Song(
                id = UNIDENTIFIED_ID,
                title = "No song is playing",
                artist = "No artist",
                audioSource = AudioSource.FromLocalFile(Uri.EMPTY),
                thumbnailSource = ThumbnailSource.FromBitmap(null),
                durationMillis = 0L
            )
        }
    }

    constructor(mediaMetadata: MediaMetadata) : this(
        id = UUID.randomUUID().toString(),
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.artist.toString(),
        audioSource = AudioSource.FromLocalFile(Uri.EMPTY),
        thumbnailSource = if (mediaMetadata.artworkUri != null) {
            ThumbnailSource
                .FromUrl(mediaMetadata.artworkUri.toString())
        } else {
            ThumbnailSource.FromBitmap(mediaMetadata.artworkData?.toBitmap()?.asImageBitmap())
        },
        durationMillis = mediaMetadata.extras?.getLong("duration") ?: 0L
    )

}


@SuppressLint("UnsafeOptInUsageError")
fun Song.toMediaItem(): MediaItem {
    val audioUri: Uri = when (audioSource) {
        is AudioSource.FromLocalFile -> audioSource.uri
        is AudioSource.FromUrl -> audioSource.url.toUri()
    }

    return MediaItem.Builder().apply {
        setUri(audioUri)
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
}