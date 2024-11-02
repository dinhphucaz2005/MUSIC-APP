package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlayListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val repository: PlayListRepository,
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    private val _playlists = MutableStateFlow<List<PlayList>>(emptyList())

    val songs = _songs.asStateFlow()
    val playlists = _playlists.asStateFlow()

    init {
        viewModelScope.launch {
            repository.savedPlayList().collect { playlists ->
                _playlists.value = playlists
            }
        }
    }

    fun savePlaylist(name: String) {
        viewModelScope.launch {
            repository.addPlaylist(name)
        }
    }


    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            repository.deletePlaylist(id)
        }
    }


}