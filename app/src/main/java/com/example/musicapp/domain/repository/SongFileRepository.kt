package com.example.musicapp.domain.repository

import com.example.musicapp.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface SongFileRepository {

    suspend fun getLocal(): StateFlow<List<Song>>
    suspend fun search(searchQuery: String): List<Song>?
    fun reload()

}