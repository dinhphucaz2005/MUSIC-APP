package com.example.mymusicapp.util

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.enums.PlaylistState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MediaControllerManager @Inject constructor(
    private val context: Context,
) {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController

    private val _isPlaying = MutableStateFlow<Boolean?>(null)
    private val _playListState = MutableStateFlow(PlaylistState.REPEAT_ALL)
    private val _currentSong = MutableStateFlow<MediaMetadata?>(null)
    private val _duration = MutableStateFlow<Long?>(null)

    val duration: StateFlow<Long?> = _duration.asStateFlow()
    val isPlaying: StateFlow<Boolean?> = _isPlaying.asStateFlow()
    val playListState: StateFlow<PlaylistState> = _playListState.asStateFlow()
    val currentSong: StateFlow<MediaMetadata?> = _currentSong.asStateFlow()


    private val controllerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            Log.d(TAG, "onMediaMetadataChanged: $mediaMetadata")
            _currentSong.value = mediaMetadata
            _duration.value = controller.duration
        }
    }

    companion object {
        private const val TAG = "MediaControllerManager"
    }

    fun initController(
        sessionToken: SessionToken,
    ) {
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                controller = controllerFuture.get()
                setupController()
                Log.d(TAG, "initController: ")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize MediaController", e)
            }
        }, MoreExecutors.directExecutor())
    }

    private fun setupController() {
        controller.playWhenReady = true
        updatePlayListState()
        controller.addListener(controllerListener)
    }

    fun updatePlayListState() {
        //get current state
        _playListState.value = when {
            controller.shuffleModeEnabled -> PlaylistState.SHUFFLE
            controller.repeatMode == Player.REPEAT_MODE_ONE -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.REPEAT_ALL
        }
        //SHUFFLE -> REPEAT_ALL -> REPEAT_ONE
        _playListState.value = when (_playListState.value) {
            PlaylistState.SHUFFLE -> PlaylistState.REPEAT_ALL
            PlaylistState.REPEAT_ALL -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.SHUFFLE
        }
        when (_playListState.value) {
            PlaylistState.SHUFFLE -> {
                controller.shuffleModeEnabled = true
                controller.repeatMode = Player.REPEAT_MODE_ALL
            }

            PlaylistState.REPEAT_ALL -> {
                controller.shuffleModeEnabled = false
                controller.repeatMode = Player.REPEAT_MODE_ALL
            }

            PlaylistState.REPEAT_ONE -> {
                controller.shuffleModeEnabled = false
                controller.repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    fun getCurrentSong(): MediaMetadata = controller.mediaMetadata

    fun playIndex(index: Int) {
        controller.seekTo(index, 0)
    }

    fun playNext() {
        controller.seekToNext()
        controller.play()
    }

    fun playPrevious() {
        controller.seekToPrevious()
        controller.play()
    }

    fun playOrPause() {
        if (controller.isPlaying) {
            controller.pause()
        } else {
            controller.play()
        }
    }


    fun getCurrentPosition(): Float { // 0 <= position <= 1
        return controller.currentPosition.toFloat() / controller.duration
    }

    fun seekTo(sliderPosition: Float) {// 0 <= sliderPosition <= 1
        controller.seekTo((controller.duration * sliderPosition).toLong())
    }

    fun isPlaying(): Boolean = controller.isPlaying

}
