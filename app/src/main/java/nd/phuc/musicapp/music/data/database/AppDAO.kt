package nd.phuc.musicapp.music.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nd.phuc.musicapp.music.data.database.entity.SongEntity

@Dao
interface AppDAO {

    @Query("SELECT * FROM song")
    fun getLikedSongs(): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(value: SongEntity)

    @Update
    suspend fun updateSong(value: SongEntity)

    @Delete
    suspend fun deleteSong(value: SongEntity)

    @Query("SELECT * FROM song WHERE filePath = :filePath LIMIT 1")
    suspend fun getSongByPath(filePath: String): SongEntity?
}
