package nd.phuc.core.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

internal interface BaseDao<T> {
    @Insert
    suspend fun insert(entity: T): Long

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)
}
