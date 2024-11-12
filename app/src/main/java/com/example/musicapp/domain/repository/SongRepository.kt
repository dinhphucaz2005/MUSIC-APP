package com.example.musicapp.domain.repository

import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song

interface SongRepository {

    suspend fun getPlayList(playListId: String): List<Song>

    suspend fun createPlayList(name: String)

    suspend fun savePlayList(id: String, name: String)

    suspend fun deletePlayList(id: String)

    suspend fun addSongs(playListId: String, songs: List<Song>)

    suspend fun deleteSongs(selectedSongIds: List<String>)

    suspend fun getLocalSong(): List<Song>

    suspend fun getPlayLists(): List<PlayList>
}