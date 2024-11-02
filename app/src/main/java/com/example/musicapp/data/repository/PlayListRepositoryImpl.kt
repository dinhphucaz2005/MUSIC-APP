package com.example.musicapp.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.data.LocalDataSource
import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.data.database.entity.PlayListEntity
import com.example.musicapp.data.database.entity.SongEntity
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.model.SongInfo
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.extension.getId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

class PlayListRepositoryImpl @Inject constructor(
    private val dao: AppDAO, private val context: Context, private val dataSource: LocalDataSource
) : PlayListRepository {

    companion object {
        private const val TAG = "PlaylistRepositoryImpl"
    }

    private val _savedPlayList = MutableStateFlow(listOf<PlayList>())
    private val _localSongFiles = MutableStateFlow(listOf<Song>())

    override suspend fun reload() {
        Log.d(TAG, "reload: ")
        try {
            getLocalFiles()
            fetchSavedPlayLists()
        } catch (e: Exception) {
            Log.d(TAG, "Error in reload: ${e.message}")
        }
    }


    override fun localFiles(): StateFlow<List<Song>> = _localSongFiles.asStateFlow()

    override fun savedPlayList(): StateFlow<List<PlayList>> = _savedPlayList.asStateFlow()

    override suspend fun deleteSongsFromPlayList(songsId: List<Long>, playlistId: Long) {
        songsId.forEach { dao.deleteSong(it) }
        val songInfos = dao.getSongsByPlayListId(playlistId).mapNotNull {
            val file = File(it.path)
            if (file.exists()) SongInfo(it.id, file.getId())
            else null
        }
        _savedPlayList.value = _savedPlayList.value.map {
            if (it.id == playlistId) it.copy(songInfos = songInfos)
            else it
        }
    }


    override suspend fun addPlaylist(name: String) {
        val id = dao.addPlayList(PlayListEntity(name = name))
        _savedPlayList.value =
            _savedPlayList.value.toMutableList().apply { add(PlayList(id, name)) }
        _savedPlayList.value.toMutableList().apply { add(PlayList(id, name)) }
    }

    override suspend fun updatePlayList(id: Long, name: String?, songs: List<Song>?) {
        name?.let { dao.updatePlayList(PlayListEntity(id, name)) }
        songs?.forEach { song ->
            song.getPath()?.let {
                dao.addSong(
                    SongEntity(
                        title = song.title, path = it, playlistId = id
                    )
                )
            }
        }
        fetchSavedPlayLists()
    }

    override suspend fun deletePlaylist(id: Long) = dao.deletePlayList(id)

    private suspend fun getLocalFiles() {
        _localSongFiles.value = dataSource.fetch(context, _localSongFiles.value)
    }

    private suspend fun fetchSavedPlayLists() {
        val songEntities = dao.getSongs()
        val playListEntities = dao.getPlayLists()
        println(songEntities)
        println(playListEntities)
        val playLists = playListEntities.map { playListEntity ->
            val songInfos = songEntities.filter { it.playlistId == playListEntity.id }.mapNotNull {
                val file = File(it.path)
                if (file.exists()) SongInfo(it.id, file.getId())
                else null
            }
            PlayList(playListEntity.id, playListEntity.name, songInfos)
        }
        _savedPlayList.value = playLists
    }

    override fun getAllSongsByPlayListId(id: Long): List<Song> {
        val localSongs = _localSongFiles.value
        val playList = _savedPlayList.value.find { it.id == id } ?: return emptyList()
        return playList.songInfos.mapNotNull { songInfo ->
            val index = dataSource.getSongInfo(songInfo.localSongId)
            if (index == null) null
            else localSongs[index].copy(id = songInfo.songId)
        }
    }

}