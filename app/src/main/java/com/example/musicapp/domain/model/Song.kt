package com.example.musicapp.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.media3.common.MediaMetadata
import com.example.musicapp.data.database.entity.SongEntity
import com.example.musicapp.extension.toBitmap

@Immutable
data class Song(
    override val id: Long = 0,
    val uri: Uri = Uri.EMPTY,
    val title: String,
    val author: String,
    val thumbnail: ImageBitmap? = null,
    val duration: Long? = null
) : Identifiable {
    companion object {
        fun unidentifiedSong(): Song {
            return Song(
                INVALID_NUMBER, Uri.EMPTY, "No song is playing", "No author", null, null
            )
        }

        private const val INVALID_NUMBER = 0L // song unavailable
    }

    fun toEntity(playListId: Long): SongEntity? {
        val path = getPath() ?: return null
        return SongEntity(0, title, path, playListId)
    }

    operator fun times(i: Int): List<Song> {
        return List(i) { this }
    }

    override fun equals(other: Any?): Boolean {
        return id == (other as? Song)?.id
    }

    fun getFileName(): String? = uri.lastPathSegment

    fun getPath(): String? = uri.path

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + (thumbnail?.hashCode() ?: 0)
        result = 31 * result + (duration?.hashCode() ?: 0)
        return result
    }

    fun update(mediaMetadata: MediaMetadata): Song {
        return copy(
            id = (id + 1) % 5,
            title = mediaMetadata.title.toString(),
            author = mediaMetadata.artist.toString(),
            thumbnail = mediaMetadata.artworkData?.toBitmap()?.asImageBitmap() ?: thumbnail
        )
    }
}