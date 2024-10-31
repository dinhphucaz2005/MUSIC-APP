package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.domain.repository.UploadRepository
import com.example.musicapp.enums.PlayingState
import com.example.musicapp.enums.PlaylistState
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val mediaController: MediaControllerManager,
    private val uploadRepository: UploadRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_SKIP_OFFSET_MILLIS = 5000L
    }

    private val _currentPlaylistSongs = MutableStateFlow<List<Song>>(emptyList())
    val currentPlaylistSongs: StateFlow<List<Song>> = _currentPlaylistSongs.asStateFlow()

    private val _isCurrentlyPlaying = MutableStateFlow(PlayingState.FALSE)
    private val _currentPlaylistState = MutableStateFlow(PlaylistState.REPEAT_ALL)
    private val _activeSong = MutableStateFlow(Song())

    val activeSong: StateFlow<Song> = _activeSong.asStateFlow()
    val isCurrentlyPlaying: StateFlow<PlayingState> = _isCurrentlyPlaying.asStateFlow()
    val currentPlaylistState: StateFlow<PlaylistState> = _currentPlaylistState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                playlistRepository.observeLocalPlaylist().collect { playlist ->
                    _currentPlaylistSongs.value = playlist?.songs ?: emptyList()
                }
            }
            launch {
                mediaController.currentSong.collect { song ->
                    _activeSong.value = song ?: Song()
                }
            }
            launch {
                mediaController.isPlaying.collect {
                    _isCurrentlyPlaying.value = PlayingState.fromBoolean(it)
                }
            }
            launch {
                mediaController.playListState.collect {
                    _currentPlaylistState.value = it
                }
            }
        }
    }

    fun playNextTrack() = mediaController.playNextSong()

    fun playPreviousTrack() = mediaController.playPreviousSong()

    fun updatePlaylistState() = mediaController.updatePlayListState()

    fun rewindTrack() = mediaController.adjustPlaybackByOffset(-DEFAULT_SKIP_OFFSET_MILLIS)

    fun fastForwardTrack() = mediaController.adjustPlaybackByOffset(DEFAULT_SKIP_OFFSET_MILLIS)

    fun playSongAtIndex(index: Int) {
        playlistRepository.setLocal(index)
        mediaController.playSongAtIndex(index)
    }

    fun togglePlayback() = mediaController.togglePlayPause()

    fun seekToSliderPosition(position: Float) =
        mediaController.seekToPosition(position) // 0 <= sliderPosition <= 1

    fun getCurrentSliderPosition() = mediaController.computePlaybackFraction() ?: 0f

    fun getCurrentTrackPosition() = mediaController.getCurrentTrackPosition() ?: 0L

    @Deprecated("Test function")
    fun uploadSongs() {
        viewModelScope.launch {
            _currentPlaylistSongs.value.forEachIndexed { index, song ->
                uploadRepository.upload(song, index)
            }
        }
    }
}
