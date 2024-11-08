package com.example.musicapp.data.repository

import com.example.musicapp.common.AppResource
import com.example.musicapp.data.FirebaseDataSource
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.CloudRepository
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : CloudRepository {

    override suspend fun loadMore(): AppResource<List<ServerSong>> = try {
        val songs = firebaseDataSource.loadMore()
        AppResource.Success(songs)
    } catch (e: Exception) {
        AppResource.Error(e.message ?: "An error occurred")
    }

    override suspend fun upload(songs: List<Song>) {
        firebaseDataSource.uploadSongs(songs)
    }
}