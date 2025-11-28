package nd.phuc.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.Song
import nd.phuc.core.domain.model.UnknownSong
import nd.phuc.core.domain.model.YoutubeSong

@Entity(
    tableName = "song",
    indices = [
        Index(value = ["filePath"], unique = true)
    ]
)
internal data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val title: String? = null,
    val filePath: String? = null,
    val isFavourite: Boolean = false,
    val playlistId: Long? = null,
) {
    companion object {
        fun fromSong(song: Song, playlistId: Long? = null): SongEntity {
            return when (song) {
                is LocalSong -> SongEntity(
                    title = song.title,
                    filePath = song.filePath,
                    isFavourite = song.isLiked,
                    playlistId = playlistId
                )

                is YoutubeSong -> TODO()
                UnknownSong -> throw IllegalArgumentException("Cannot convert UnknownSong to SongEntity")
            }
        }
    }
}