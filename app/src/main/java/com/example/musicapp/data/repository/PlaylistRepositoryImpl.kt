package com.example.musicapp.data.repository

import android.content.Context
import com.example.musicapp.data.LocalDataSource
import com.example.musicapp.data.RoomDataSource
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.repository.PlayListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class PlayListRepositoryImpl @Inject constructor(
    private val context: Context,
    private val roomDataSource: RoomDataSource,
    private val localDataSource: LocalDataSource
) : PlayListRepository {

    private val _localPlayList = MutableStateFlow(PlayList.getInvalidPlayList())
    private val _savedPlayLists = MutableStateFlow(listOf<PlayList>())

    override suspend fun reload() {
        try {
            val currentSongs = _localPlayList.value.songs
            val newSongs = localDataSource.fetch(context)

            val updateLocalPlayList = {
                _localPlayList.value = PlayList.getLocalPlayList(newSongs)
            }

            if (currentSongs.size != newSongs.size) updateLocalPlayList()
            else {
                for (index in currentSongs.indices) {
                    if (currentSongs[index] != newSongs[index]) {
                        updateLocalPlayList()
                        break
                    }
                }
            }

            _savedPlayLists.value = roomDataSource.getSavedPlayLists(newSongs)
        } catch (e: Exception) {
            EventBus.getDefault().postSticky("Error in reload: ${e.message}")
        }
    }

    override fun getLocalPlayList(): StateFlow<PlayList> = _localPlayList.asStateFlow()

    override fun getSavedPlayLists(): StateFlow<List<PlayList>> = _savedPlayLists.asStateFlow()

    override fun getPlayList(playListId: Long): PlayList? {
        return _savedPlayLists.value.find { it.id == playListId }
    }

    override suspend fun createPlayList(name: String) {
        roomDataSource.createNewPlayList(name).let {
            _savedPlayLists.value = _savedPlayLists.value.toMutableList().apply { add(it) }
        }
    }

    override suspend fun savePlayList(id: Long, name: String) {
        _savedPlayLists.value = _savedPlayLists.value.toMutableList()
            .map { if (it.id == id) it.copy(name = name) else it }
        roomDataSource.savePlayList(id, name)
    }

    override suspend fun deletePlayList(id: Long) {
        _savedPlayLists.value = _savedPlayLists.value.toMutableList().filter { it.id != id }
        roomDataSource.deletePlayList(id)
    }

    override suspend fun addSongs(
        playListId: Long, selectedSongIds: List<Long>
    ) {
        val localSongs = _localPlayList.value.songs
        roomDataSource.addSongs(selectedSongIds.mapNotNull { id ->
            localSongs.find { it.id == id }
        }, playListId)
        val newPlayList = roomDataSource.getPlayList(playListId, localSongs) ?: return
        _savedPlayLists.value = _savedPlayLists.value.toMutableList()
            .map { if (it.id == playListId) newPlayList else it }
    }

    override suspend fun deleteSongs(playListId: Long, selectedSongIds: List<Long>) {
        _savedPlayLists.update {
            it.map { playList ->
                if (playList.id == playListId) {
                    playList.copy(songs = playList.songs.filter { song -> song.id !in selectedSongIds })
                } else playList
            }
        }
        roomDataSource.deleteSongs(selectedSongIds)
    }
}