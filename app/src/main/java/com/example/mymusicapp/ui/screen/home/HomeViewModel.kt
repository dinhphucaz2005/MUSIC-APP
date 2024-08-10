package com.example.mymusicapp.ui.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SongFileRepository,
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {

    private val _currentSong = mutableStateOf<MediaMetadata?>(null)
    val currentSong: State<MediaMetadata?> = _currentSong

    private val _isPlayingState = mutableStateOf<Boolean?>(null)
    val isPlayingState: State<Boolean?> = _isPlayingState

    val songList = mutableStateListOf<Song>()

    init {
        mediaControllerManager.setOnIsPlayingChangedListener { isPlaying ->
            _isPlayingState.value = isPlaying
        }
        mediaControllerManager.setOnMediaMetadataChangedListener { song ->
            _currentSong.value = song
        }
        viewModelScope.launch {
            repository.getLocal().collect {
                songList.clear()
                songList.addAll(it)
            }
        }
    }

    fun playNext() {
        mediaControllerManager.playNext()
    }

    fun search(searchQuery: String) {
        viewModelScope.launch {
            songList.clear()
            repository.search(searchQuery)?.let { songList.addAll(it) }
        }
    }

    fun reload() {
        viewModelScope.launch {
            repository.reload()
        }
    }

    fun playIndex(index: Int) {
        mediaControllerManager.playIndex(index)
    }

    fun playOrPause() {
        mediaControllerManager.playOrPause()
    }


}