package com.example.musicapp.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import com.example.musicapp.data.database.entity.SongEntity

@Immutable
data class Song(
    override val id: Long,
    val uri: Uri,
    val title: String,
    val author: String,
    val smallBitmap: ImageBitmap? = null,
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

    fun getFileName() = uri.lastPathSegment?.substringAfterLast("/")

    fun getPath(): String? = uri.path

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + (smallBitmap?.hashCode() ?: 0)
        result = 31 * result + (duration?.hashCode() ?: 0)
        return result
    }
}
