package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaController: MediaControllerManager, repository: PlayListRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_SKIP_OFFSET_MILLIS = 5000L
    }


    val playList = repository.getLocalPlayList()
    val currentSong = mediaController.currentSong
    val playBackState = mediaController.playBackState


    fun playNextTrack() = mediaController.playNextSong()

    fun playPreviousTrack() = mediaController.playPreviousSong()

    fun updatePlaylistState() = mediaController.updatePlayListState()

    fun rewindTrack() = mediaController.adjustPlaybackByOffset(-DEFAULT_SKIP_OFFSET_MILLIS)

    fun fastForwardTrack() = mediaController.adjustPlaybackByOffset(DEFAULT_SKIP_OFFSET_MILLIS)

    fun playSongAtIndex(index: Int) = mediaController.playSongLocal(index)

    fun togglePlayback() = mediaController.togglePlayPause()

    fun seekToSliderPosition(position: Float) =
        mediaController.seekToPosition(position) // 0 <= sliderPosition <= 1

    fun getCurrentSliderPosition() = mediaController.computePlaybackFraction() ?: 0f

    fun getCurrentTrackPosition() = mediaController.getCurrentTrackPosition() ?: 0L

    fun uploadSongs() {
        TODO("Not yet implemented")
    }
}
