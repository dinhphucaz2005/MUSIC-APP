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
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MediaControllerManager @Inject constructor(
    private val context: Context,
) {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController

    private var onIsPlayingChanged: ((Boolean) -> Unit)? = null
    private var onMediaMetadataChanged: ((MediaMetadata) -> Unit)? = null
    private var onPlayListStateChanged: ((PlaylistState) -> Unit)? = null

    private val controllerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            onIsPlayingChanged?.invoke(isPlaying)
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            Log.d(TAG, "onMediaMetadataChanged: $mediaMetadata")
            onMediaMetadataChanged?.invoke(mediaMetadata)
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            super.onShuffleModeEnabledChanged(shuffleModeEnabled)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
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

    private fun updatePlayListState() {
        var currentPlaylistState = when {
            controller.shuffleModeEnabled -> PlaylistState.SHUFFLE
            controller.repeatMode == Player.REPEAT_MODE_ONE -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.REPEAT_ALL
        }
        //SHUFFLE -> REPEAT_ALL -> REPEAT_ONE
        currentPlaylistState = when (currentPlaylistState) {
            PlaylistState.SHUFFLE -> PlaylistState.REPEAT_ALL
            PlaylistState.REPEAT_ALL -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.SHUFFLE
        }
        when (currentPlaylistState) {
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


    fun getCurrentPosition(): Float {
        return controller.currentPosition.toFloat() / controller.duration
    }

    fun seekTo(sliderPosition: Float) {
        controller.seekTo((controller.duration * sliderPosition).toLong())
    }

    fun setOnIsPlayingChangedListener(listener: (Boolean) -> Unit) {
        onIsPlayingChanged = listener
    }

    fun setOnMediaMetadataChangedListener(listener: (MediaMetadata) -> Unit) {
        onMediaMetadataChanged = listener
    }

    fun setOnPlayListStateChangedListener(listener: (PlaylistState) -> Unit) {
        onPlayListStateChanged = listener
    }
}
