package com.example.mymusicapp.data.repository

import com.example.mymusicapp.callback.ResultCallback
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.database.entity.PlaylistEntity
import com.example.mymusicapp.domain.model.Playlist
import com.example.mymusicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    val database: AppDatabase
) : PlaylistRepository {

    private val appDao = database.appDAO()

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val result = appDao.getAllPlayLists().map {
                Playlist.create(it)
            }
            _playlists.value = result
        }
    }

    override fun getPlaylist(): StateFlow<List<Playlist>> = _playlists

    override suspend fun addPlaylist(playlist: Playlist, onResult: ResultCallback<String>?) {
        try {
            val playlistEntity = PlaylistEntity(name = playlist.name)
            val id = appDao.addPlayList(playlistEntity)
            onResult?.onSuccess("Playlist added with ID $id")
        } catch (e: Exception) {
            onResult?.onFailure(e)
        }
    }

    override suspend fun removePlaylist(playlist: Playlist, onResult: ResultCallback<String>?) {
        try {
            appDao.deleteSongById(playlist.id)
            appDao.deletePlayList(playlist.id)
            onResult?.onSuccess("Playlist removed")
        } catch (e: Exception) {
            onResult?.onFailure(e)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist, onResult: ResultCallback<String>?) {
        try {
            appDao.updatePlayList(PlaylistEntity(playlist.id, playlist.name))
            onResult?.onSuccess("Playlist updated")
        } catch (e: Exception) {
            onResult?.onFailure(e)
        }
    }
}


