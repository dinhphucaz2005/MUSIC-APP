package com.example.musicapp.domain.repository

import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

interface PlayListRepository {

    suspend fun reload()

    suspend fun addPlaylist(name: String)

    suspend fun deletePlaylist(id: Long)

    suspend fun updatePlayList(id: Long, name: String?, songs: List<Song>?)

    suspend fun deleteSongsFromPlayList(songsId: List<Long>, playlistId: Long)

    fun localFiles(): StateFlow<List<Song>>

    fun savedPlayList(): StateFlow<List<PlayList>>

    fun getAllSongsByPlayListId(id: Long): List<Song>
}