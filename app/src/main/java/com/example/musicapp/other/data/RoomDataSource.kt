package com.example.musicapp.other.data

import com.example.musicapp.other.data.database.AppDAO
import com.example.musicapp.other.data.database.entity.PlayListEntity
import com.example.musicapp.other.data.database.entity.SongEntity
import com.example.musicapp.other.data.database.entity.toEntity
import com.example.musicapp.other.domain.model.Song
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSource @Inject constructor(
    private val dao: AppDAO
) {

    suspend fun createNewPlayList(name: String) {
        val id = UUID.randomUUID().toString()
        dao.addPlayList(PlayListEntity(id, name))
    }


    suspend fun savePlayList(id: String, name: String) {
        dao.updatePlayList(PlayListEntity(id, name))
    }

    suspend fun addSongs(songs: List<Song>, playListId: String) {
        songs.forEach { song ->
            song.toEntity(playListId)?.let { dao.addSong(it) }
        }
    }

    suspend fun deletePlayList(id: String) = dao.deletePlayList(id)

    suspend fun deleteSongs(selectedSongIds: List<String>) {
        selectedSongIds.forEach { dao.deleteSong(it) }
    }

    suspend fun getPlayList(playListId: String): List<SongEntity> {
        return dao.getSongsByPlayListId(playListId)
    }

    suspend fun getPlayLists(): List<PlayListEntity> = dao.getPlayLists()

}
