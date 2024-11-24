package com.example.musicapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
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
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val mediaControllerManager: MediaControllerManager,
    private val repository: SongRepository,
) : ViewModel() {

    data class PlayListItem(
        val data: PlayList,
        val isSelected: Boolean = false,
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
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

    fun savePlaylist(playlistId: String, playlistName: String, selectedSongs: List<Song>) =
        load {
            repository.savePlayList(playlistId, playlistName)
            repository.addSongsToPlaylist(
                playlistId, selectedSongs
            )
            loadSavedPlayLists()
        }

    fun play(index: Int) {
        val queue = Queue.Builder().setSavePlayListSong(_songs.value).build()
        mediaControllerManager.playQueue(queue, index)
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
        _activePlayList.update { _playlists.value.find { it.data.id == playlistId }?.data }
        _songs.update { repository.getSongsFromPlaylist(playlistId) }
    }

    fun getPlaylistName(playlistId: String): String {
        return _playlists.value.find { it.data.id == playlistId }?.data?.name ?: ""
    }
}