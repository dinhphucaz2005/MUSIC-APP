package com.example.musicapp.domain.repository

import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.model.Song

interface CloudRepository {

    @Deprecated("No longer used")
    suspend fun loadSongs(title: String, page: Int, size: Int): AppResource<List<Song>>

    suspend fun loadMore(): AppResource<List<ServerSong>>

    suspend fun upload(song: Song)
}