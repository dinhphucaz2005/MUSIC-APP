package com.example.musicapp.data

import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.data.database.entity.PlayListEntity
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.getId
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSource @Inject constructor(
    private val dao: AppDAO
) {
    suspend fun getSavedPlayLists(localSongs: List<Song>): List<PlayList> {
        val localSongMap = localSongs.associateBy { it.id }

        val playListEntities = dao.getPlayLists()
        if (playListEntities.isEmpty()) return emptyList()

        val songEntities = dao.getSongs()

        val playLists = mutableListOf<PlayList>()

        playListEntities.forEach { playListEntity ->
            val songsInPlaylist = songEntities.filter { it.playlistId == playListEntity.id }
                .mapNotNull { songEntity ->
                    val file = File(songEntity.path)
                    if (file.exists()) {
                        localSongMap[file.getId()]?.copy(id = songEntity.id)
                    } else null
                }
            playLists.add(PlayList(playListEntity.id, playListEntity.name, songsInPlaylist))
        }
        return playLists
    }

    suspend fun createNewPlayList(name: String): PlayList {
        val id = dao.addPlayList(PlayListEntity(0, name))
        return PlayList(id, name)
    }

    suspend fun savePlayList(id: Long, name: String) {
        dao.updatePlayList(PlayListEntity(id, name))
    }

    suspend fun addSongs(newSongs: List<Song>, playListId: Long) {
        newSongs.forEach { song ->
            song.toEntity(playListId)?.let { dao.addSong(it) }
        }
    }

    suspend fun deletePlayList(id: Long) {
        dao.deletePlayList(id)
    }

    suspend fun getPlayList(id: Long, localSongs: List<Song>): PlayList? {
        val songEntities = dao.getSongsByPlayListId(id).mapNotNull {
            val file = File(it.path)
            if (file.exists()) {
                localSongs.find { song -> song.id == file.getId() }?.copy(id = it.id)
            } else null
        }
        val playListEntity = dao.getPlayList(id) ?: return null
        return PlayList(playListEntity.id, playListEntity.name, songEntities)
    }

    suspend fun deleteSongs(selectedSongIds: List<Long>) {
        selectedSongIds.forEach { dao.deleteSong(it) }
    }

}
