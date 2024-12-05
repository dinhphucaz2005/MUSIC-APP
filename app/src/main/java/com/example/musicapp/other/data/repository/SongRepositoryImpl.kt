package com.example.musicapp.other.data.repository

import android.content.Context
import com.example.musicapp.other.data.LocalDataSource
import com.example.musicapp.other.data.RoomDataSource
import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.repository.SongRepository
import com.example.musicapp.extension.getId
import java.io.File
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val context: Context,
    private val roomDataSource: RoomDataSource,
    private val localDataSource: LocalDataSource
) : SongRepository {

    override suspend fun getSongsFromPlaylist(playListId: String): List<Song> {
        val localSong = localDataSource.get(context)
        return roomDataSource.getPlayList(playListId).mapNotNull {
            val file = File(it.path)
            if (file.exists()) {
                localSong.find { song -> song.id == file.getId() }?.copy(id = it.id)
            } else null
        }
    }

    override suspend fun createPlayList(name: String) {
        roomDataSource.createNewPlayList(name)
    }

    override suspend fun savePlayList(id: String, name: String) {
        roomDataSource.savePlayList(id, name)
    }

    override suspend fun deletePlayList(id: String) = roomDataSource.deletePlayList(id)

    override suspend fun addSongsToPlaylist(playListId: String, songs: List<Song>) =
        roomDataSource.addSongs(songs, playListId)

    override suspend fun deleteSongs(selectedSongIds: List<String>) =
        roomDataSource.deleteSongs(selectedSongIds)

    override suspend fun getLocalSong(): List<Song> = localDataSource.get(context)

    override suspend fun getPlayLists(): List<PlayList> = roomDataSource.getPlayLists().map {
        PlayList(id = it.id, name = it.name)
    }
}