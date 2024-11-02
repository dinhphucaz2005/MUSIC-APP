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
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.enums.PlaylistState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var currentPlayList = PlayList.INVALID_PLAYLIST

    private val _isPlaying = MutableStateFlow<Boolean?>(null)
    private val _playListState = MutableStateFlow(PlaylistState.REPEAT_ALL)
    private val _currentSong = MutableStateFlow<Song?>(null)

    val isPlaying: StateFlow<Boolean?> = _isPlaying.asStateFlow()
    val playListState: StateFlow<PlaylistState> = _playListState.asStateFlow()
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    init {
        coroutineScope.launch {
            observeLocalFiles()
            observeSavedPlayLists()
        }
    }

    private fun CoroutineScope.observeLocalFiles() = launch {
        repository.localFiles().collect {
            loadSongs(currentPlayList)
        }
    }

    private fun CoroutineScope.observeSavedPlayLists() = launch {
        repository.savedPlayList().collect {
            loadSongs(currentPlayList)
        }
    }

    private val controllerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            val index = controller?.currentMediaItemIndex ?: return
            if (currentPlayList.id != PlayList.LOCAL_ID) {
//                _currentSong.value = repository.getAllSongsByPlayListId(currentPlayList.id)[index]
            } else {
//                _currentSong.value = repository.localFiles().value[index]
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

        // Load initial playlist if available
        loadSongs(PlayList.LOCAL_PLAYLIST)
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
        // Get current playlist state
        val currentState = when {
            shuffleModeEnabled -> PlaylistState.SHUFFLE
            repeatMode == Player.REPEAT_MODE_ONE -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.REPEAT_ALL
        }
        // Cycle through the playlist state
        val nextState = when (currentState) {
            PlaylistState.SHUFFLE -> PlaylistState.REPEAT_ALL
            PlaylistState.REPEAT_ALL -> PlaylistState.REPEAT_ONE
            else -> PlaylistState.SHUFFLE
        }
        // Update controller with next state
        when (nextState) {
            PlaylistState.SHUFFLE -> {
                shuffleModeEnabled = true
                repeatMode = Player.REPEAT_MODE_ALL
            }

            PlaylistState.REPEAT_ALL -> {
                shuffleModeEnabled = false
                repeatMode = Player.REPEAT_MODE_ALL
            }

            else -> {
                shuffleModeEnabled = false
                repeatMode = Player.REPEAT_MODE_ONE
            }
        }
        _playListState.value = nextState
    }

    fun playSongAtIndex(index: Int, playlist: PlayList = currentPlayList) = withControllerPlay {
        if (playlist.id == currentPlayList.id) {
            seekTo(index, 0)
            play()
        } else {
            loadSongs(playlist)
            playlist.index?.let {
                seekTo(it, 0)
                play()
            }
        }
    }

    fun playNextSong() = withControllerPlay { seekToNext() }

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

    fun loadSongs(playlist: PlayList) = withController {
        mainScope.launch {
            clearMediaItems()
            if (playlist.id == PlayList.LOCAL_ID) {
                repository.localFiles().value.forEach { song ->
                    addMediaItem(song)
                }
            } else {
                repository.getAllSongsByPlayListId(playlist.id).forEach { song ->
                    addMediaItem(song)
                }
            }
            prepare()
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
