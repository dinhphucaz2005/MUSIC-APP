package com.example.musicapp.other.domain.model

import android.annotation.SuppressLint
import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.core.model.ThumbnailSource
import com.example.core.util.MILLIS_IN_HOUR
import com.example.core.util.MILLIS_IN_MINUTE
import com.example.core.util.MILLIS_IN_SECOND
import com.example.player.model.Song



data class FirebaseSong(
    override val id: String,
    val title: String,
    val artist: String,
    val audioUrl: String,
    val thumbnailSource: ThumbnailSource,
    val durationMillis: Long?,
) : Song() {

    override fun getAudio(): Uri = this.audioUrl.toUri()

    override fun getSongTitle(): String = this.title

    override fun getSongArtist(): String = this.artist

    override fun getThumbnail(): ThumbnailSource = this.thumbnailSource

    @SuppressLint("DefaultLocale")
    override fun getDuration(): String {
        return if (this.durationMillis == null || this.durationMillis == 0L) {
            "--:--"
        } else {
            val hours = this.durationMillis / MILLIS_IN_HOUR
            val minutes = (this.durationMillis % MILLIS_IN_HOUR) / MILLIS_IN_MINUTE
            val seconds = (this.durationMillis % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND

            when {
                hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
                else -> String.format("%02d:%02d", minutes, seconds)
            }
        }
    }

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

}

