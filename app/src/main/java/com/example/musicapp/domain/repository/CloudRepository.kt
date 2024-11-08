package com.example.musicapp.domain.repository

import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.model.Song

interface CloudRepository {

    suspend fun loadMore(): AppResource<List<ServerSong>>

    suspend fun upload(songs: List<Song>)
}