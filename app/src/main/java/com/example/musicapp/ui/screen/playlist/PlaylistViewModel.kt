package com.example.musicapp.ui.screen.playlist

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: PlaylistRepository,
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {

    fun savePlaylist(name: String) {
        viewModelScope.launch {
            repository.addPlaylist(name)
        }
    }

    fun setCurPlaylist(playlistId: Long?) {
        _playlistId = playlistId
        _curPlaylist.value = _playlists.value.find { it.id == playlistId }
    }

    fun playTrackAtIndex(index: Int) {
        _curPlaylist.value?.id?.let { repository.setPlaylist(it, index) }
        mediaControllerManager.playSongAtIndex(index)
    }

    fun deleteSelectedSongs(selectedIndices: SnapshotStateList<Boolean>) {
        _curPlaylist.value?.let { curPlaylist ->
            val deleteSongIndex = mutableListOf<Int>()
            selectedIndices.forEachIndexed { index, isSelected ->
                if (isSelected) deleteSongIndex.add(index)
            }
            viewModelScope.launch {
                repository.deleteSongs(deleteSongIndex, curPlaylist.id)
                _curPlaylist.value = _playlists.value.find { it.id == curPlaylist.id }
            }
        }
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            repository.deletePlaylist(id)
        }
    }


    private var _playlistId: Long? = null
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    private val _curPlaylist = MutableStateFlow<Playlist?>(null)

    val playlists = _playlists.asStateFlow()
    val curPlaylist = _curPlaylist.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAllPlaylistsFromDatabase().collect { playlists ->
                _playlists.value = playlists
            }
        }
    }
}