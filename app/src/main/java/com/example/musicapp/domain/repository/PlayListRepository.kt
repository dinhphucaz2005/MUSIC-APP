package com.example.musicapp.domain.repository

import com.example.musicapp.domain.model.PlayList
import kotlinx.coroutines.flow.StateFlow

interface PlayListRepository {

    suspend fun reload()

    fun getLocalPlayList(): StateFlow<PlayList>

    fun getSavedPlayLists(): StateFlow<List<PlayList>>

    fun getPlayList(playListId: Long): PlayList?

    suspend fun createPlayList(name: String)

    suspend fun savePlayList(id: Long, name: String)

    suspend fun deletePlayList(id: Long)

    suspend fun addSongs(playListId: Long, selectedSongIds: List<Long>)

    suspend fun deleteSongs(playListId: Long, selectedSongIds: List<Long>)
}