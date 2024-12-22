package nd.phuc.cache.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.innertube.pages.PlaylistPage

@Entity(tableName = "playlist")
internal data class PlaylistEntity(
    @PrimaryKey val id: String,
    val content: PlaylistPage?,
    val timestamp: Long = System.currentTimeMillis()
)
