package com.example.mymusicapp.ui.screen.song

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaMetadata
import com.example.mymusicapp.enums.PlaylistState
import com.example.mymusicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {

    private val _isPlayingState = mutableStateOf<Boolean?>(null)
    val isPlayingState: State<Boolean?> = _isPlayingState

    private val _playListState = mutableStateOf(PlaylistState.REPEAT_ALL)
    val playListState: State<PlaylistState> = _playListState

    private val _currentSong = mutableStateOf<MediaMetadata?>(null)
    val currentSong: State<MediaMetadata?> = _currentSong

    init {
        mediaControllerManager.setOnIsPlayingChangedListener { isPlaying ->
            _isPlayingState.value = isPlaying
        }

        mediaControllerManager.setOnMediaMetadataChangedListener { song ->
            _currentSong.value = song
        }

        mediaControllerManager.setOnPlayListStateChangedListener { state ->
            _playListState.value = state
        }
    }

    fun playIndex(index: Int) {
        mediaControllerManager.playIndex(index)
    }

    fun playNext() {
        mediaControllerManager.playNext()
    }

    fun playPrevious() {
        mediaControllerManager.playPrevious()
    }

    fun playOrPause() {
        mediaControllerManager.playOrPause()
    }

    fun changePlayListState() {
        TODO("Not yet implemented")
    }

    fun getCurrentPosition(): Float {
        return mediaControllerManager.getCurrentPosition()
    }

    fun seekTo(sliderPosition: Float) {
        mediaControllerManager.seekTo(sliderPosition)
    }

}