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

    private val _playList = MutableStateFlow<PlayList?>(null)
    private val _inSelectionMode = MutableStateFlow(false)

    val songs = _songs.asStateFlow()
    val selectedItems = _selectedItems.asStateFlow()
    val playList = _playList.asStateFlow()
    val inSelectionMode = _inSelectionMode.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getSavedPlayLists().collect {
                loadPlayList(playList.value?.id)
            }
        }
    }


    fun loadPlayList(playlistId: Long?) {
        playlistId ?: return
        _playList.value = repository.getPlayList(playlistId)
    }


    fun toggleSelection(id: Long) {
        _selectedItems.value = _selectedItems.value.toMutableMap().apply {
            this[id] = !(this[id] ?: false)
        }
    }


    fun playTrack(index: Int) {
        playList.value?.let {
            mediaControllerManager.playSongAtIndex(index, it.id)
        }
    }

    fun deleteSelectedSongs() {
        val selectedIds = _selectedItems.value.filter { it.value }.keys.toList()
        viewModelScope.launch {
//            repository.deleteSongsFromPlayList(
//                selectedIds,
//                currentPlayList.value?.id ?: return@launch
//            )
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