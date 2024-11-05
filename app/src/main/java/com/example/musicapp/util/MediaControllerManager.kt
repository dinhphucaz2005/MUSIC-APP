package com.example.musicapp.util

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.domain.model.PlayBackState
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.enums.LoopMode
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MediaControllerManager @Inject constructor(
    private val context: Context, private val repository: PlayListRepository
) {

    companion object {
        const val TAG = "MediaControllerManager"
    }

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private var controller: MediaController? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val backgroundScope = CoroutineScope(Dispatchers.IO)

    private val _playBackState = MutableStateFlow(PlayBackState())
    val playBackState = _playBackState.asStateFlow()

    private val _currentSong = MutableStateFlow(Song.unidentifiedSong())
    val currentSong: StateFlow<Song> = _currentSong.asStateFlow()

    private var currentPlayList = PlayList.getInvalidPlayList()

    init {
        backgroundScope.launch {
            observeLocalFiles()
            observeSavedPlayList()
        }
    }

    private fun CoroutineScope.observeLocalFiles() = launch {
        repository.getLocalPlayList().collect {
            if (it != currentPlayList) loadSongsAndPlay(it)
        }
    }

    private fun CoroutineScope.observeSavedPlayList() = launch {
        repository.getSavedPlayLists().collect { playLists ->
            for (playList in playLists) {
                if (playList.id == currentPlayList.id) {
                    if (playList != currentPlayList) loadSongsAndPlay(playList)
                }
            }
        }
    }


    private val controllerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playBackState.update { it.updatePlayerState(isPlaying) }
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            withController {
                val index = currentMediaItemIndex
                _currentSong.update { currentPlayList.songs.getOrNull(index) ?: return }
            }
        }
    }

    fun initializeMediaController(sessionToken: SessionToken) {
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                controller = controllerFuture.get().apply {
                    playWhenReady = true
                    addListener(controllerListener)
                }
                updatePlayListState()
            } catch (e: Exception) {
                Log.d(TAG, "initializeMediaController: ${e.message}")
            }
        }, MoreExecutors.directExecutor())
        loadSongsAndPlay(currentPlayList)
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

    private fun playAtIndex(index: Int) = withController {
        seekTo(index, 0)
        play()
    }

    fun playSongLocalPlayList(index: Int) = withController {
        if (currentPlayList.id == PlayList.LOCAL_ID) playAtIndex(index)
        else loadSongsAndPlay(repository.getLocalPlayList().value, index)
    }

    fun playSongInSavedPlayList(index: Int, playList: PlayList) = withController {
        if (currentPlayList.id != playList.id) loadSongsAndPlay(playList, index)
        else playAtIndex(index)
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

    private fun loadSongsAndPlay(playlist: PlayList, index: Int = 0) {
        currentPlayList = playlist
        withController {
            mainScope.launch {
                clearMediaItems()
                for (song in playlist.songs) addMediaItem(song)
                prepare()
                playAtIndex(index)
            }
        }
    }

    private fun addMediaItem(song: Song) = withController {
        val mediaMetadata = MediaMetadata.Builder().apply {
            setTitle(song.title)
            setArtist(song.author)
        }.build()
        val mediaItem = MediaItem.Builder().apply {
            setUri(song.uri)
            setMediaMetadata(mediaMetadata)
        }.build()
        addMediaItem(mediaItem)
    }
}
