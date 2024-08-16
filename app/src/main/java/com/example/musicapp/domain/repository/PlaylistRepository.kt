package com.example.musicapp.domain.repository

import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Playlist
import kotlinx.coroutines.flow.StateFlow

interface PlaylistRepository {

    fun getPlaylist(): StateFlow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist, onResult: ResultCallback<String>? = null)

    suspend fun removePlaylist(playlist: Playlist, onResult: ResultCallback<String>? = null)

    suspend fun updatePlaylist(playlist: Playlist, onResult: ResultCallback<String>? = null)

}