package com.example.musicapp.music.presentation.ui.screen.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.music.domain.model.LocalSong
import com.example.musicapp.music.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    // Define states for song loading
    sealed class SongState {
        object Idle : SongState()
        object Loading : SongState()
        data class Success(val songs: List<LocalSong>) : SongState()
        data class Error(val message: String) : SongState()
    }

    private val _songState = MutableStateFlow<SongState>(SongState.Idle)
    val songState: StateFlow<SongState> = _songState

    fun loadSongs() {
        viewModelScope.launch {
            _songState.value = SongState.Loading
            try {
                val songs = songRepository.getLocalSong()
                _songState.value = SongState.Success(songs)
            } catch (e: Exception) {
                _songState.value = SongState.Error(e.message ?: "Unknown error")
            }
        }
    }
}