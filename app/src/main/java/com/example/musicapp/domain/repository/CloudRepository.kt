package com.example.musicapp.domain.repository

import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.Song

interface CloudRepository {

    suspend fun loadSongs(title: String, page: Int, size: Int): AppResource<List<Song>>
}