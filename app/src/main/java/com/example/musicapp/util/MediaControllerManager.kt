package com.example.musicapp.util

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.enums.PlaylistState
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

    fun initializeMediaController(
        sessionToken: SessionToken,
    ) {
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                controller = controllerFuture.get()
                controller.playWhenReady = true
                updatePlayListState()
                controller.addListener(controllerListener)

                Log.d(TAG, "initController: ")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize MediaController", e)
            }
        }, MoreExecutors.directExecutor())
    }

    fun updatePlayListState() {
        // Determine the new playlist state
        val newState = when {
            controller.shuffleModeEnabled -> PlaylistState.SHUFFLE
            controller.repeatMode == Player.REPEAT_MODE_ONE -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.REPEAT_ALL
        }

        // Update the playlist state
        _playListState.value = when (newState) {
            PlaylistState.SHUFFLE -> PlaylistState.REPEAT_ALL
            PlaylistState.REPEAT_ALL -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.SHUFFLE
        }

        // Apply the new playlist state to the controller
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

    fun getCurrentSongIndex(): Int? {
        if (!::controller.isInitialized) return null
        return controller.currentMediaItemIndex
    }

    fun playSongAtIndex(index: Int) {
        controller.seekTo(index, 0)
        controller.play()
    }

    fun playNextSong() {
        controller.seekToNext()
        controller.play()
    }

    fun playPreviousSong() {
        controller.seekToPrevious()
        controller.play()
    }

    fun togglePlayPause() {
        if (controller.isPlaying) {
            controller.pause()
        } else {
            controller.play()
        }
    }

    fun getCurrentPlaybackPosition(): Float { // 0 <= position <= 1
        return controller.currentPosition.toFloat() / controller.duration
    }

    fun seekToPosition(position: Float) { // 0 <= position <= 1
        controller.seekTo((controller.duration * position).toLong())
    }

    fun adjustPlaybackByOffset(offsetMillis: Long) {
        val newPosition =
            (controller.currentPosition + offsetMillis).coerceIn(0L, controller.duration)
        controller.seekTo(newPosition)
    }
}
