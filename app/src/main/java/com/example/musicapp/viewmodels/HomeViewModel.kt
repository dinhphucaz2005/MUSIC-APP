package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.constants.DEFAULT_DURATION_MILLIS
import com.example.musicapp.domain.model.Queue
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.SongRepository
import com.example.musicapp.extension.load
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaControllerManager: MediaControllerManager,
    private val repository: SongRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading
        .onStart { if (_songs.value.isEmpty()) loadSong() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(DEFAULT_DURATION_MILLIS),
            false
        )

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()


    private fun loadSong() = load(_isLoading) {
        _songs.update { repository.getLocalSong() }
        delay(2000)
    }

    fun play(index: Int) {
        val queue = Queue.Builder().setLocalSong(_songs.value).build()
        mediaControllerManager.playQueue(queue, index)
    }
}