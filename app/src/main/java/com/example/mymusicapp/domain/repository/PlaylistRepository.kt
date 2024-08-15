package com.example.mymusicapp.domain.repository

import com.example.mymusicapp.callback.ResultCallback
import com.example.mymusicapp.data.database.entity.PlaylistEntity
import com.example.mymusicapp.domain.model.Playlist
import kotlinx.coroutines.flow.StateFlow

interface PlaylistRepository {

    fun getPlaylist(): StateFlow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist, onResult: ResultCallback<String>? = null)

    suspend fun removePlaylist(playlist: Playlist, onResult: ResultCallback<String>? = null)

    suspend fun updatePlaylist(playlist: Playlist, onResult: ResultCallback<String>? = null)

}