package com.example.mymusicapp.domain.repository

import com.example.mymusicapp.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface SongFileRepository {

    suspend fun getLocal(): StateFlow<List<Song>>
    suspend fun search(searchQuery: String): List<Song>?
    fun reload()

}