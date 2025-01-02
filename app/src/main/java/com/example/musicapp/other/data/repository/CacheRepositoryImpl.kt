package com.example.musicapp.other.data.repository

import com.example.innertube.models.SongItem
import com.example.musicapp.other.data.database.CacheDao
import com.example.musicapp.other.data.database.entity.SearchEntity
import com.example.musicapp.other.data.database.entity.SearchType
import com.example.musicapp.other.domain.repository.CacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CacheRepositoryImpl(
    private val cacheDao: CacheDao
) : CacheRepository {

    override suspend fun insertSearch(query: String, result: List<SongItem>) {

        val id = cacheDao.getSearchId(query)

        val resultJson = Json.encodeToString(result)

        val searchEntity = SearchEntity(
            id = id,
            searchQuery = query,
            result = resultJson,
            type = SearchType.SONGS,
            timestamp = System.currentTimeMillis()
        )
        cacheDao.insertSearch(searchEntity)
    }

    override fun getRecentSearches(): Flow<List<SearchEntity>> = cacheDao.getRecentSearches()


}