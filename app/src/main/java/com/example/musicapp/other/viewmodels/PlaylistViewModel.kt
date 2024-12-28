package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.extension.withIOContext
import com.example.musicapp.other.domain.model.Playlist
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
) : ViewModel() {

    data class PlaylistItem(
        val data: Playlist, val isSelected: Boolean = false
    )

    private val _isLoading = MutableStateFlow(false)

    val playlists = songRepository
        .getPlayLists()
        .map { it.map { playlist -> PlaylistItem(playlist) } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val isLoading = _isLoading.asStateFlow()


    fun createNewPlayList(name: String) {
        withIOContext {
            songRepository.createPlayList(name)
        }
    }

    fun deleteLikedSong(song: Song) {
        withIOContext {
            songRepository.unlikeSong(song)
        }
    }

}