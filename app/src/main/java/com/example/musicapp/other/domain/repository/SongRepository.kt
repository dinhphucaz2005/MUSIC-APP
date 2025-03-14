package com.example.musicapp.other.domain.repository

import com.example.musicapp.other.domain.model.LocalSong
import com.example.player.model.Playlist
import com.example.player.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {

    suspend fun getSongsFromPlaylist(playlistId: Int): List<Song>

    suspend fun createPlayList(name: String)

    suspend fun updatePlaylist(playlistId: Int, name: String)

    suspend fun deletePlayList(id: Int)

    suspend fun addSongsToPlaylist(playListId: Int, localSongs: List<Song>)

    suspend fun deleteSongs(songIds: List<String>)

    suspend fun getLocalSong(): List<LocalSong>

    fun getPlayLists(): Flow<List<Playlist>>

    suspend fun likeSong(song: Song)

    suspend fun unlikeSong(song: Song)

    fun getLikedSongs(): Flow<List<Song>>

}