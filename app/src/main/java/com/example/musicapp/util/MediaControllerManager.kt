package com.example.musicapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
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
import com.example.musicapp.extension.withIOContext
import com.example.musicapp.extension.withMainContext
import com.example.musicapp.other.domain.model.CurrentSong
import com.example.musicapp.other.domain.model.PlayBackState
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.YoutubeSong
import com.example.musicapp.other.domain.repository.SongRepository
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
    private val binder: MusicService.MusicBinder?,
    private val songRepository: SongRepository
) : Player.Listener {

    private val controllerFuture = binder?.service?.getSession()?.token?.let {
        MediaController
        .Builder(context, it)
        .buildAsync()
    }


    private var controller: MediaController? = null

    val queue: StateFlow<Queue?>
        get() = binder?.service?.queueFlow ?: MutableStateFlow(null)


    private val _playBackState = MutableStateFlow(PlayBackState())
    val playBackState = _playBackState.asStateFlow()

    private val _currentSong = MutableStateFlow(CurrentSong.unidentifiedSong())
    val currentSong: StateFlow<CurrentSong> = _currentSong.asStateFlow()


    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        val index = getCurrentMediaIndex()
        if (index != null) {
            val song = queue.value?.songs?.get(index) ?: return
            val isLiked = song.getAudio().let { likedSongsSet.contains(it) }
            _currentSong.update { CurrentSong(song, isLiked) }
        }
    }

    private var likedSongsSet: Set<Uri> = emptySet()

    init {
        Log.d(TAG, "init")
        withIOContext {
            songRepository.getLikedSongs().collect { likedSongs ->
                likedSongsSet = likedSongs.map { it.getAudio() }.toSet()
                val song = if (queue.value?.songs?.isEmpty() == true) {
                    CurrentSong.unidentifiedSong()
                } else {
                    val index = runBlocking(Dispatchers.Main) {
                        getCurrentMediaIndex()
                    } ?: 0
                    val song = queue.value?.songs?.get(index) ?: return@collect
                    val isLiked = song.getAudio().let { likedSongsSet.contains(it) }
                    CurrentSong(song, isLiked)
                }
                val isLiked = song.data.getAudio().let { likedSongsSet.contains(it) }
                _currentSong.update { CurrentSong(song.data, isLiked) }
            }
        }
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

            if (repeatMode == Player.REPEAT_MODE_OFF) {
                repeatMode = Player.REPEAT_MODE_ALL
            }

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
        _playBackState.value = _playBackState.value.updateLoopMode()
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

    fun playYoutubeSong(song: SongItem) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = runBlocking(Dispatchers.IO) {
                CustomYoutube.next(WatchEndpoint(song.id))
            }

            val songs = response
                .getOrNull()
                ?.items ?: emptyList()

            withMainContext {
                playQueue(
                    songs = songs.map { YoutubeSong(it) },
                    index = 0,
                    Queue.YOUTUBE_SONG_ID + "/" + song.id
                )
            }
        }
    }


    private fun playMediaItems(queue: List<MediaItem>, index: Int = 0) = withController {
        clearMediaItems()
        addMediaItems(queue)
        seekTo(index, 0)
        play()
    }

    fun playQueue(songs: List<Song>, index: Int = 0, id: String) = withControllerPlay {
        if (queue.value?.id == id) {
            seekTo(index, 0)
            play()
        } else {
            playMediaItems(songs.map { it.toMediaItem() }, index)
            binder?.service?.updateQueue(Queue(songs = songs, id = id, index = index))
        }
    }

    fun downLoadCurrentSong() {
        binder?.service?.downloadCurrentSong()
    }

    fun toggleLikedCurrentSong() {
        withIOContext {
            val song = currentSong.value
            if (song.isLiked) {
                songRepository.unlikeSong(song.data)
                _currentSong.update { CurrentSong(song.data, false) }
            } else {
                songRepository.likeSong(song.data)
                _currentSong.update { CurrentSong(song.data, true) }
            }
        }
    }

    fun addToNext(song: Song) {
        binder?.service?.addToNext(song)
    }

    fun addToQueue(song: Song) {
        binder?.service?.addToQueue(song)
    }

    fun getCurrentMediaIndex(): Int? = withController { currentMediaItemIndex }

}