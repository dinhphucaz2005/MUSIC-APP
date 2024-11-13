package com.example.musicapp.util

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.domain.model.AudioSource
import com.example.musicapp.domain.model.PlayBackState
import com.example.musicapp.domain.model.Queue
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.model.toMediaItem
import com.example.musicapp.enums.LoopMode
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MediaControllerManager @Inject constructor(
    private val context: Context
) : Player.Listener {

    companion object {
        const val TAG = "MediaControllerManager"
    }

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    private val _playBackState = MutableStateFlow(PlayBackState())
    val playBackState = _playBackState.asStateFlow()

    private val _activeSong = MutableStateFlow(Song.unidentifiedSong())
    val activeSong: StateFlow<Song> = _activeSong.asStateFlow()

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playBackState.update { it.updatePlayerState(isPlaying) }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_READY) {
            withController {
                _activeSong.update { Song(mediaMetadata, duration) }
            }
        }
    }

    fun initializeMediaController(sessionToken: SessionToken) {
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            try {
                controller = controllerFuture?.get()?.apply {
                    playWhenReady = true
                    addListener(this@MediaControllerManager)
                }
                updatePlayListState()
            } catch (e: Exception) {
                Log.d(TAG, "initializeMediaController: ${e.message}")
            }
        }, MoreExecutors.directExecutor())
    }

    @MainThread
    private inline fun <T> withController(action: MediaController.() -> T?): T? {
        return controller?.run(action)
    }

    @MainThread
    private inline fun <T> withControllerPlay(action: MediaController.() -> T?): Unit? {
        return controller?.run {
            action(this)
            play()
        }
    }

    fun updatePlayListState() = withController {
        _playBackState.update { it.updateLoopMode() }
//         Update controller with new loop mode
        when (_playBackState.value.loopMode) {
            LoopMode.SHUFFLE -> {
                shuffleModeEnabled = true
                repeatMode = Player.REPEAT_MODE_ALL
            }

            LoopMode.REPEAT_ALL -> {
                shuffleModeEnabled = false
                repeatMode = Player.REPEAT_MODE_ALL
            }

            LoopMode.REPEAT_ONE -> {
                shuffleModeEnabled = false
                repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    fun playNextSong() = withControllerPlay {
        if (hasNextMediaItem()) seekToNext() else seekTo(0, 0)
    }

    fun playPreviousSong() = withControllerPlay { seekToPrevious() }

    fun togglePlayPause() = withController { if (isPlaying) pause() else play() }

    fun computePlaybackFraction(): Float? = withController {
        if (duration == 0L) 0f else currentPosition.toFloat() / duration
    }

    fun seekToPosition(position: Float) = withController {
        seekTo((duration * position).toLong())
    }

    fun adjustPlaybackByOffset(offsetMillis: Long) = withController {
        val newPosition = (currentPosition + offsetMillis).coerceIn(0L, duration)
        seekTo(newPosition)
    }

    fun getCurrentTrackPosition(): Long? = withController { currentPosition }

    private fun playAtIndex(index: Int) = withController {
        seekTo(index, 0)
        play()
    }

    private var currentQueue = Queue.UNIDENTIFIED_ID

    fun playQueue(queue: Queue, index: Int = 0) = withController {
        if (currentQueue == queue.id) playAtIndex(index)
        else {
            clearMediaItems()
            setMediaItems(queue.songs.map {
                Log.d(TAG, "playQueue: $it")
                it.toMediaItem()
            })
            prepare()
            playAtIndex(index)
            currentQueue = queue.id
        }
    }
}