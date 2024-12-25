package com.example.musicapp.other.domain.repository

import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.model.Song

interface SongRepository {

    suspend fun getSongsFromPlaylist(playListId: String): List<LocalSong>

    suspend fun getSongByPath(path: String): LocalSong?

    suspend fun createPlayList(name: String)

    suspend fun savePlayList(id: String, name: String)

    suspend fun deletePlayList(id: String)

    suspend fun addSongsToPlaylist(playListId: String, localSongs: List<LocalSong>)

    suspend fun deleteSongs(selectedSongIds: List<String>)

    suspend fun getLocalSong(): List<LocalSong>

    suspend fun getPlayLists(): List<PlayList>

    suspend fun likeSong(song: Song)

    suspend fun unlikeSong(id: Long)

    suspend fun getLikedSongs(): List<Song>
}