package com.example.musicapp.other.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicapp.other.data.database.entity.SearchEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchEntity)

    @Query("SELECT * FROM searches ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<SearchEntity>>

    @Query("SELECT * FROM searches WHERE id = :id")
    suspend fun getSearch(id: Int): SearchEntity?

    @Query("SELECT id FROM searches WHERE searchQuery = :query")
    suspend fun getSearchId(query: String): Int?
}