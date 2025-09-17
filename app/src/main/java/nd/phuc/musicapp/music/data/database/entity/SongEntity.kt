package nd.phuc.musicapp.music.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String? = null,
    val filePath: String? = null,
)