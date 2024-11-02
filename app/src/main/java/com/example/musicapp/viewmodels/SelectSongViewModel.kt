package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlayListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectSongViewModel @Inject constructor(
    private val playlistRepository: PlayListRepository
) : ViewModel() {

    private val _localSongs = MutableStateFlow<List<Pair<Song, Boolean>>>(emptyList())

    val localSongs = _localSongs.asStateFlow()

    init {
        viewModelScope.launch {
            playlistRepository.localFiles().value.let {
                _localSongs.value = it.map { song ->
                    Pair(song, false)
                }
            }
        }
    }


    fun togglePlaylist(index: Int) {
        _localSongs.value = _localSongs.value.toMutableList().apply {
            this[index] = Pair(this[index].first, !this[index].second)
        }
    }

    fun savePlaylist(id: Long, name: String) {
        viewModelScope.launch {
            val songs = _localSongs.value.filter { it.second }.map { it.first }
            playlistRepository.updatePlayList(id, name, songs)
        }
    }

    fun getPlaylistName(playlistId: Long): String =
        playlistRepository.savedPlayList().value.find { it.id == playlistId }?.name
            ?: ""
}