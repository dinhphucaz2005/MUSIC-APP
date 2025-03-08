package com.example.player.model

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.core.model.ThumbnailSource
import java.util.UUID

abstract class Song(
) {

    abstract val id: String

    abstract fun getSongTitle(): String

    abstract fun getSongArtist(): String

    abstract fun getThumbnail(): ThumbnailSource

    abstract fun getDuration(): String

    abstract fun toMediaItem(): MediaItem

    abstract fun getArtistId(): String?

    companion object {

        fun unidentifiedSong(id: String = UUID.randomUUID().toString()): Song {
            return object : Song() {

                override fun getAudio(): Uri {
                    return Uri.EMPTY
                }

                override fun getArtistId(): String? {
                    return null
                }

                override val id: String = id

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

    abstract fun getAudio(): Uri
}