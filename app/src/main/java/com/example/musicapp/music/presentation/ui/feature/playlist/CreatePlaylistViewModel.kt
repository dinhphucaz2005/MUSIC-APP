package com.example.musicapp.music.presentation.ui.feature.playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var playlistName by mutableStateOf("")
        private set

    var playlistDescription by mutableStateOf("")
        private set

    var thumbnailUrl by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var creationSuccess by mutableStateOf(false)
        private set

    var creationError by mutableStateOf<String?>(null)
        private set

    fun onPlaylistNameChange(name: String) {
        playlistName = name
        creationError = null
    }

    fun onPlaylistDescriptionChange(description: String) {
        playlistDescription = description
    }

    fun onThumbnailSelected(url: String?) {
        thumbnailUrl = url
    }

    fun createPlaylist() {
        if (playlistName.isBlank()) {
            creationError = "Playlist name cannot be empty"
            return
        }

        isLoading = true

        viewModelScope.launch {
            try {
                playlistRepository.createPlaylist(
                    name = playlistName.trim(),
                    description = playlistDescription.trim(),
                    thumbnailUrl = thumbnailUrl
                )
                creationSuccess = true
            } catch (e: Exception) {
                creationError = e.message ?: "Failed to create playlist"
            } finally {
                isLoading = false
            }
        }
    }
}