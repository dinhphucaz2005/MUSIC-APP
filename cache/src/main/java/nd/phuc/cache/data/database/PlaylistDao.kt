package nd.phuc.cache.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist WHERE id = :id")
    suspend fun getPlaylist(id: String): PlaylistEntity?

}