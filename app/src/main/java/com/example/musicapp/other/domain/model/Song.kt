package com.example.musicapp.other.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.media3.common.MediaMetadata
import com.example.musicapp.extension.toBitmap
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
        fun unidentifiedSong(id: String? = null): Song {
            return Song(
                id = id ?: UNIDENTIFIED_ID,
                title = "No song is playing", artist = "No artist",
                audioSource = AudioSource.FromLocalFile(Uri.EMPTY),
                thumbnailSource = ThumbnailSource.FromBitmap(null),
                durationMillis = 0L
            )
        }
    }

    constructor(mediaMetadata: MediaMetadata, durationMillis: Long? = null) : this(
        id = UUID.randomUUID().toString(),
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.artist.toString(),
        audioSource = AudioSource.FromLocalFile(Uri.EMPTY),
        thumbnailSource = if (mediaMetadata.artworkUri != null) {
            ThumbnailSource.FromUrl(mediaMetadata.artworkUri.toString())
        } else {
            ThumbnailSource.FromBitmap(mediaMetadata.artworkData?.toBitmap()?.asImageBitmap())
        },
        durationMillis = durationMillis ?: 0L
    )

}