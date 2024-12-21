package com.example.musicapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.innertube.CustomYoutube
import com.example.innertube.models.SongItem
import com.example.innertube.models.WatchEndpoint
import com.example.musicapp.constants.LoopMode
import com.example.musicapp.constants.PlayerState
import com.example.musicapp.extension.toMediaItem
import com.example.musicapp.extension.toMediaItemFromYT
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
    private val binder: MusicService.MusicBinder?
) : Player.Listener {

    private val controllerFuture = binder?.service?.getSession()?.token?.let {
        MediaController
        .Builder(context, it)
        .buildAsync()
    }

    private var controller: MediaController? = null

    val queue : StateFlow<Queue?>
        get() = binder?.service?.queueFlow ?: MutableStateFlow(null)

    private val _playBackState = MutableStateFlow(PlayBackState())
    val playBackState = _playBackState.asStateFlow()

    private val _currentSong = MutableStateFlow(Song.unidentifiedSong())
    val currentSong = _currentSong.asStateFlow()

    init {
        controllerFuture?.addListener({
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
        withController {
            _playBackState.update { PlayBackState(
                PlayerState.fromBoolean(isPlaying),
                LoopMode.fromInt(repeatMode, shuffleModeEnabled)
            ) }
        }
    }

    companion object {
        const val TAG = "MediaControllerManager"
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playBackState.update { it.updatePlayerState(isPlaying) }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        binder?.service?.updateIndex(controller?.currentMediaItemIndex ?: 0)
        _currentSong.value =
            queue.value?.getSong(controller?.currentMediaItemIndex) ?: Song.unidentifiedSong()
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

    fun playAtIndex(index: Int) = withController {
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

    fun playYoutubeSong(song: SongItem) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = runBlocking(Dispatchers.IO) {
                CustomYoutube.next(WatchEndpoint(song.id))
            }

            val songs = response
                .getOrNull()
                ?.items
                ?.toMutableList() ?: mutableListOf()

            CoroutineScope(Dispatchers.Main).launch {
                playQueue(Queue.Youtube(song.id, 0, songs))
            }
        }
    }

    fun playYoutubePlaylist(playlistId: String, songs: List<SongItem>, index: Int = 0) {
        playQueue(Queue.Youtube(playlistId, index, songs))
    }

    fun seekToIndex(index: Int)= withController {
        seekTo(index, 0)
    }


    private fun playMediaItems(queue: List<MediaItem>, index: Int = 0) = withController {
        clearMediaItems()
        addMediaItems(queue)
        seekTo(index, 0)
        play()
    }

    fun playQueue(newQueue: Queue) = withControllerPlay {
        binder?.service?.updateQueue(newQueue)
        when (newQueue) {
            is Queue.Other -> playMediaItems(
                newQueue.songs.map { it.toMediaItem() },
                newQueue.index
            )

            is Queue.Youtube -> playMediaItems(
                newQueue.songs.map { it.toMediaItemFromYT() },
                newQueue.index
            )
        }
    }
}