package com.example.musicapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.innertube.CustomYoutube
import com.example.innertube.models.SongItem
import com.example.innertube.models.WatchEndpoint
import com.example.musicapp.constants.LoopMode
import com.example.musicapp.extension.toMediaItem
import com.example.musicapp.extension.toMediaItemFromYT
import com.example.musicapp.extension.toSong
import com.example.musicapp.other.domain.model.PlayBackState
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.service.MusicService
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("UnsafeOptInUsageError")
class MediaControllerManager(
    context: Context,
    binder: MusicService.MusicBinder,
    scope: CoroutineScope
) : Player.Listener {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val controllerFuture = MediaController
        .Builder(context, binder.service.getSession().token)
        .buildAsync()

    private var controller: MediaController? = null

    init {
        controllerFuture.addListener({
            try {
                controller = controllerFuture.get().apply {
                    playWhenReady = true
                    addListener(this@MediaControllerManager)
                }
                updatePlayListState()
            } catch (e: Exception) {
                Log.e(TAG, "Error creating MediaController", e)
            }
        }, MoreExecutors.directExecutor())
    }

    companion object {
        const val TAG = "MediaControllerManager"
    }


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

    private fun adjustPlaybackByOffset(offsetMillis: Long) = withController {
        val newPosition = (currentPosition + offsetMillis).coerceIn(0L, duration)
        seekTo(newPosition)
    }

    fun getCurrentTrackPosition(): Long? = withController { currentPosition }

    private fun playAtIndex(index: Int) = withController {
        seekTo(index, 0)
        play()
    }

    private var currentQueue = Queue.UNIDENTIFIED_ID

    private fun playQueue(queue: List<MediaItem>, index: Int = 0) = withController {
        clearMediaItems()
        addMediaItems(queue)
        seekTo(index, 0)
        play()
    }

    fun dispose() {
        controller?.removeListener(this)
    }

    fun seekToSliderPosition(sliderPosition: Float) {
        withController {
            seekTo((duration * sliderPosition).toLong())
        }
    }

    fun fastForwardTrack() {
        adjustPlaybackByOffset(5000L)
    }

    fun rewindTrack() {
        adjustPlaybackByOffset(-5000L)
    }

    fun playLocalSong(songs: List<Song>, index: Int) {
        _songs.update { songs }
        playQueue(songs.map { it.toMediaItem() }, index)
    }

    fun playYoutubeSong(song: SongItem) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = runBlocking(Dispatchers.IO) {
                CustomYoutube.next(WatchEndpoint(song.id))
            }

            val newSongs = response
                .getOrNull()
                ?.items
                ?.toMutableList() ?: mutableListOf()

            _songs.update { newSongs.map { it.toSong() } }

            CoroutineScope(Dispatchers.Main).launch {
                playQueue(newSongs.map { it.toMediaItemFromYT() }, 0)
            }
        }
    }

    fun seekToIndex(index: Int)= withController {
        seekTo(index, 0)
    }
}