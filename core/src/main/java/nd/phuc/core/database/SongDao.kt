package nd.phuc.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import nd.phuc.core.database.entity.SongEntity

@Dao
internal interface SongDao : BaseDao<SongEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(value: SongEntity)

    @Query("SELECT * FROM song ORDER BY id DESC")
    fun getSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM song WHERE isFavourite = 1")
    fun getLikedSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM song WHERE filePath = :filePath LIMIT 1")
    suspend fun getSongByPath(filePath: String): SongEntity?

    @Query("UPDATE song SET isFavourite = :isFavourite WHERE filePath = :filePath")
    suspend fun updateFavourite(filePath: String, isFavourite: Boolean)

    @Transaction
    suspend fun toggleLike(filePath: String) {
        val song = getSongByPath(filePath)
        if (song != null)
            updateFavourite(filePath, !song.isFavourite)
        else
            addSong(SongEntity(filePath = filePath, isFavourite = true))
    }
}
