package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Queue
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.SongRepository
import com.example.musicapp.extension.load
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val mediaControllerManager: MediaControllerManager,
    private val repository: SongRepository,
) : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _localSongs = MutableStateFlow<List<Song>>(emptyList())
    val localSongs = _localSongs.asStateFlow()

    private val _activePlayList = MutableStateFlow<PlayList?>(null)
    val activePlayList = _activePlayList.asStateFlow()

    private fun loadSavedPlayLists() = load {
        _playlists.update { repository.getPlayLists() }
    }

    private val _playlists = MutableStateFlow<List<PlayList>>(emptyList())
    val playlists = _playlists.asStateFlow()

    init {
        viewModelScope.launch {
            val result = repository.getPlayLists()
            _playlists.update {
                result
            }
        }
    }


    fun createNewPlayList(name: String) = load {
        repository.createPlayList(name)
        loadSavedPlayLists()
    }

    fun deletePlayList(id: String) = load {
        repository.deletePlayList(id)
        loadSavedPlayLists()
    }

    fun loadPlayList(playlistId: String?, isReload: Boolean = true) {
        if (playlistId == null) return

        load(if (isReload) _isLoading else null) {
            _activePlayList.update {
                _playlists.value.find { it.id == playlistId }
            }
            _songs.update { repository.getPlayList(playlistId) }
        }
    }

    fun deleteSongs(selectedSongIds: List<String>) = load {
        activePlayList.value?.id.let {
            repository.deleteSongs(selectedSongIds)
            loadPlayList(it, false)
        }
    }

    fun updatePlayListName(value: String) = _activePlayList.update { it?.copy(name = value) }

    fun savePlaylist(activePlayList: PlayList?, selectedSongs: List<String>) = load {
        if (activePlayList == null) return@load
        repository.savePlayList(activePlayList.id, activePlayList.name)
        repository.addSongs(
            activePlayList.id,
            _localSongs.value.filter { selectedSongs.contains(it.id) })
        loadPlayList(activePlayList.id, false)
        loadSavedPlayLists()
    }

    fun play(index: Int) {
        val queue = Queue.Builder().setSavePlayListSong(_songs.value).build()
        mediaControllerManager.playQueue(queue, index)
    }

    fun loadLocalSongs() = load(_isLoading) {
        _localSongs.update { repository.getLocalSong() }
    }
}