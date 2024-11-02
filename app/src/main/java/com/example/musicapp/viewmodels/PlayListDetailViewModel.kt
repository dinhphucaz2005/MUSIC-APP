package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListDetailViewModel @Inject constructor(
    private val repository: PlayListRepository,
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    private val _selectedItems = MutableStateFlow<Map<Long, Boolean>>(emptyMap())

    private val _currentPlayList = MutableStateFlow<PlayList?>(null)
    private val _inSelectionMode = MutableStateFlow(false)

    val songs = _songs.asStateFlow()
    val selectedItems = _selectedItems.asStateFlow()
    val currentPlayList = _currentPlayList.asStateFlow()
    val inSelectionMode = _inSelectionMode.asStateFlow()

    init {
        viewModelScope.launch {
            repository.savedPlayList().collect {
                loadPlayList(currentPlayList.value?.id)
            }
        }
    }


    fun loadPlayList(playlistId: Long?) {
        playlistId ?: return
        val songs = repository.getAllSongsByPlayListId(playlistId)
        _songs.value = songs
        println(songs)
        _currentPlayList.value = repository.savedPlayList().value.find { it.id == playlistId }
        _selectedItems.value = songs.associateBy({ it.id }, { false })
    }


    fun toggleSelection(id: Long) {
        _selectedItems.value = _selectedItems.value.toMutableMap().apply {
            this[id] = !(this[id] ?: false)
        }
    }


    fun playTrack(songId: Long) {
        currentPlayList.value?.let {
            mediaControllerManager.loadSongs(it.copy(index = _songs.value.indexOfFirst { song -> song.id == songId }))
        }
    }

    fun deleteSelectedSongs() {
        val selectedIds = _selectedItems.value.filter { it.value }.keys.toList()
        viewModelScope.launch {
            repository.deleteSongsFromPlayList(
                selectedIds,
                currentPlayList.value?.id ?: return@launch
            )
        }
        exitSelectionMode()
    }

    fun exitSelectionMode() {
        _inSelectionMode.value = false
        _selectedItems.value = emptyMap()
    }


    fun startSelectionMode() {
        _inSelectionMode.value = true
    }

}