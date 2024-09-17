package com.example.musicapp.ui.screen.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectSongViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _localSongs = MutableStateFlow<List<Pair<Song, Boolean>>>(emptyList())

    val localSongs = _localSongs.asStateFlow()

    init {
        viewModelScope.launch {
            playlistRepository.observeLocalPlaylist().value?.let {
                _localSongs.value =
                    MutableList(it.songs.size) { index -> Pair(it.songs[index], false) }
            }
        }
    }


    fun togglePlaylist(index: Int) {
        _localSongs.value = _localSongs.value.mapIndexed { i, pair ->
            if (i == index) {
                Pair(pair.first, !pair.second)
            } else {
                pair
            }
        }
    }

    fun savePlaylist(id: Long, name: String) {
        viewModelScope.launch {
            val songs = _localSongs.value.filter { it.second }.map { it.first }
            playlistRepository.savePlaylist(id, name, songs)
        }
    }

    fun getPlaylistName(playlistId: Long): String =
        playlistRepository.observeAllPlaylistsFromDatabase().value.find { it.id == playlistId }?.name
            ?: ""
}