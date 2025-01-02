package com.example.musicapp.other.domain.repository

import com.example.innertube.models.SongItem
import com.example.musicapp.other.data.database.entity.SearchEntity
import kotlinx.coroutines.flow.Flow

interface CacheRepository {

    suspend fun insertSearch(query: String, result: List<SongItem>)

    fun getRecentSearches(): Flow<List<SearchEntity>>


}