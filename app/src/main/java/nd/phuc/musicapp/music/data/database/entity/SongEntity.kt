package nd.phuc.musicapp.music.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "song"
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String? = null,
    val audioSource: String,
    val thumbnail: String? = null,
    val type: Int,
    val filePath: String? = null,
    val durationMillis: Long? = null,
    @ColumnInfo(name = "playlist_id") val playlistId: Int,
)