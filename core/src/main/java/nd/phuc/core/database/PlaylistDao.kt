package nd.phuc.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nd.phuc.core.database.entity.PlaylistEntity

@Dao
internal interface PlaylistDao : BaseDao<PlaylistEntity> {
    @Query("SELECT * FROM playlist ORDER BY name ASC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Insert
    suspend fun addPlaylist(playlist: PlaylistEntity): Long
}
