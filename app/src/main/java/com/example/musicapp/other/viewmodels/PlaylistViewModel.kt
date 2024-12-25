package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import com.example.musicapp.extension.load
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: SongRepository,
) : ViewModel() {

    data class PlayListItem(
        val data: PlayList,
        val isSelected: Boolean = false,
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _songs = MutableStateFlow<List<LocalSong>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _activePlayList = MutableStateFlow<PlayList?>(null)
    val activePlayList = _activePlayList.asStateFlow()

    private fun loadSavedPlayLists() = load(_isLoading) {
        _playlists.update { repository.getPlayLists().map { PlayListItem(data = it) } }
    }

    private val _playlists = MutableStateFlow<List<PlayListItem>>(emptyList())
    val playlists = _playlists.asStateFlow()

    init {
        loadSavedPlayLists()
    }


    fun createNewPlayList(name: String) = load {
        repository.createPlayList(name)
        loadSavedPlayLists()
    }

    fun deletePlayList() = load {
        _playlists.value.forEach {
            if (it.isSelected) repository.deletePlayList(it.data.id)
        }
        _playlists.update { it.filter { playlist -> !playlist.isSelected } }
    }

    fun savePlaylist(playlistId: String, playlistName: String, selectedExSongs: List<LocalSong>) =
        load {
            repository.savePlayList(playlistId, playlistName)
            repository.addSongsToPlaylist(
                playlistId, selectedExSongs
            )
            loadSavedPlayLists()
        }

    fun deleteSongs(selectedSongIds: List<String>) = load {
        repository.deleteSongs(selectedSongIds)
    }

    fun togglePlayList(id: String) {
        _playlists.update {
            it.map { playlist ->
                if (playlist.data.id == id) playlist.copy(isSelected = !playlist.isSelected)
                else playlist
            }
        }
    }

    fun loadPlaylist(playlistId: String) = load {
//        _activePlayList.update { _playlists.value.find { it.data.id == playlistId }?.data }
//        _songs.update { repository.getSongsFromPlaylist(playlistId) }
    }

    fun getPlaylistName(playlistId: String): String {
        return _playlists.value.find { it.data.id == playlistId }?.data?.name ?: ""
    }
}