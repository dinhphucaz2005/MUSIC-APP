package com.example.musicapp.music.domain.repository

import com.example.musicapp.music.data.database.entity.PlaylistEntity
import com.example.musicapp.music.data.database.entity.SongEntity
import com.example.musicapp.music.domain.model.LocalSong
import com.example.musicapp.music.domain.model.Playlist
import com.example.musicapp.music.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {

    @Deprecated("No longer used")
    suspend fun getSongsFromPlaylist(playlistId: Int): List<Song>

    @Deprecated("No longer used")
    suspend fun createPlayList(name: String)

    @Deprecated("No longer used")
    suspend fun updatePlaylist(playlistId: Int, name: String)

    @Deprecated("No longer used")
    suspend fun deletePlayList(id: Int)

    @Deprecated("No longer used")
    suspend fun addSongsToPlaylist(playListId: Int, localSongs: List<Song>)

    @Deprecated("No longer used")
    suspend fun deleteSongs(songIds: List<String>)

    @Deprecated("No longer used")
    suspend fun getLocalSong(): List<LocalSong>

    fun getPlayLists(): Flow<List<Playlist>>

    @Deprecated("No longer used")
    suspend fun likeSong(song: Song)

    @Deprecated("No longer used")
    suspend fun unlikeSong(song: Song)

    fun getLikedSongs(): Flow<List<Song>>

    @Deprecated("No longer used")
    suspend fun savePlaylist(
        name: String,
        description: String,
        createdBy: String,
        songs: List<SongEntity>
    )

    @Deprecated("No longer used")
    suspend fun getPlaylists(): List<PlaylistEntity>

}