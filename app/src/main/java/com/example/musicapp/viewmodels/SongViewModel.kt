package com.example.musicapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model.AudioSource
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.CloudRepository
import com.example.musicapp.extension.load
import com.example.musicapp.util.FirebaseKey
import com.example.musicapp.util.MediaControllerManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val mediaController: MediaControllerManager,
    private val cloudRepository: CloudRepository,
) : ViewModel() {

    companion object {
        private const val DEFAULT_SKIP_OFFSET_MILLIS = 5000L
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val activeSong = mediaController.activeSong
    val playBackState = mediaController.playBackState

    fun playNextTrack() = mediaController.playNextSong()

    fun playPreviousTrack() = mediaController.playPreviousSong()

    fun updatePlaylistState() = mediaController.updatePlayListState()

    fun rewindTrack() = mediaController.adjustPlaybackByOffset(-DEFAULT_SKIP_OFFSET_MILLIS)

    fun fastForwardTrack() = mediaController.adjustPlaybackByOffset(DEFAULT_SKIP_OFFSET_MILLIS)

    fun togglePlayback() = mediaController.togglePlayPause()

    fun seekToSliderPosition(position: Float) =
        mediaController.seekToPosition(position) // 0 <= sliderPosition <= 1

    fun getCurrentSliderPosition() = mediaController.computePlaybackFraction() ?: 0f

    fun getCurrentTrackPosition() = mediaController.getCurrentTrackPosition() ?: 0L

    fun uploadSongs(songs: List<Song>) = cloudRepository.upload(songs)
}
