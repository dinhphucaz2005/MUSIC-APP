package com.example.mymusicapp.domain.repository

import com.example.mymusicapp.domain.model.Playlist

interface PlaylistRepository {

    suspend fun getPlaylist(): List<Playlist>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun removePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

}