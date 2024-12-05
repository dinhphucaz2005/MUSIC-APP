package com.example.musicapp.other.domain.repository

import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.model.Song

interface SongRepository {

    suspend fun getSongsFromPlaylist(playListId: String): List<Song>

    suspend fun createPlayList(name: String)

    suspend fun savePlayList(id: String, name: String)

    suspend fun deletePlayList(id: String)

    suspend fun addSongsToPlaylist(playListId: String, songs: List<Song>)

    suspend fun deleteSongs(selectedSongIds: List<String>)

    suspend fun getLocalSong(): List<Song>

    suspend fun getPlayLists(): List<PlayList>
}