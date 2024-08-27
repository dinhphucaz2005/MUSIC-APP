package com.example.musicapp.domain.repository

import com.example.musicapp.callback.AppResource
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface PlaylistRepository {

    suspend fun reload(): AppResource<Nothing>

    suspend fun addPlaylist(name: String)

    suspend fun savePlaylist(id: Long, name: String, songs: List<Song>)

    suspend fun deleteSongs(deleteSongIndex: MutableList<Int>, id: Long)

    suspend fun deletePlaylist(id: Long)

    fun setLocal(index: Int)

    fun setPlaylist(playlistId: Long, index: Int)

    fun observeCurrentPlaylist(): StateFlow<Playlist?>

    fun observeLocalPlaylist(): StateFlow<Playlist?>

    fun observeAllPlaylistsFromDatabase(): StateFlow<List<Playlist>>
}