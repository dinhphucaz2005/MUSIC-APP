package com.example.musicapp.music.presentation.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.music.domain.model.LocalSong
import com.example.musicapp.music.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
) : ViewModel() {

    private val _localSongState = MutableStateFlow<LocalSongState>(LocalSongState.Idle)
    val localSongState: StateFlow<LocalSongState> = _localSongState.asStateFlow()


    fun loadSongs() {
        _localSongState.value = LocalSongState.Loading
        viewModelScope.launch {
            val songs = songRepository.getLocalSong()
            _localSongState.value = LocalSongState.Success(songs, System.currentTimeMillis())
        }
    }

    sealed class LocalSongState {

        object Idle : LocalSongState()

        object Loading : LocalSongState()

        data class Success(val songs: List<LocalSong>, val lastReloadMillis: Long) :
            LocalSongState()

        data class Error(val message: String) : LocalSongState()
    }

}

