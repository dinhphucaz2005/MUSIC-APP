package nd.phuc.core.helper

import android.content.Context
import androidx.annotation.MainThread
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.Playlist
import nd.phuc.core.domain.model.Song
import nd.phuc.core.domain.model.ThumbnailSource
import nd.phuc.core.domain.model.UnknownSong
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import nd.phuc.core.service.music.MusicService
import timber.log.Timber
import kotlin.random.Random

class MediaControllerManager(
    private val songRepository: LocalSongRepository,
) {
    enum class PlayerState {
        PLAYING,
        PAUSED,
        STOPPED;
    }

    enum class RepeatState {
        OFF,
        ONE,
        ALL
    }

    enum class ShuffleState {
        OFF,
        ON
    }

    val playerListener = object : Player.Listener {
        override fun onMediaItemTransition(
            mediaItem: MediaItem?,
            reason: Int,
        ) {
            super.onMediaItemTransition(mediaItem, reason)
            val currentIndex = controller?.currentMediaItemIndex ?: return
            val currentPlaylist = _playlist.value ?: return
            if (currentIndex < 0 || currentIndex >= currentPlaylist.second.size) return
            _currentSongId.value = currentPlaylist.second[currentIndex]
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int,
        ) {
            Timber.d("onPositionDiscontinuity: $reason")
            _position.value = controller?.currentPosition ?: 0L
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Timber.d("onIsPlayingChanged: $isPlaying")
            _playerState.value = if (isPlaying) {
                PlayerState.PLAYING
            } else {
                PlayerState.PAUSED
            }
            _duration.value = controller?.duration ?: 0L
        }

        // listener for update position
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            Timber.d("onTimelineChanged: $reason")
            _position.value = controller?.currentPosition ?: 0L
            _duration.value = controller?.duration ?: 0L
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            Timber.d("onPlaybackStateChanged: $playbackState")
            _playerState.value = when (playbackState) {
                Player.STATE_BUFFERING -> PlayerState.PAUSED
                Player.STATE_READY -> {
                    if (controller?.isPlaying == true) {
                        PlayerState.PLAYING
                    } else {
                        PlayerState.PAUSED
                    }
                }

                Player.STATE_IDLE -> PlayerState.STOPPED
                Player.STATE_ENDED -> PlayerState.STOPPED
                else -> PlayerState.STOPPED
            }
            _duration.value = controller?.duration ?: 0L

        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            Timber.d("onShuffleModeEnabledChanged: $shuffleModeEnabled")
            _shuffleState.value = if (shuffleModeEnabled) {
                ShuffleState.ON
            } else {
                ShuffleState.OFF
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            Timber.d("onRepeatModeChanged: $repeatMode")
            _repeatState.value = when (repeatMode) {
                Player.REPEAT_MODE_OFF -> RepeatState.OFF
                Player.REPEAT_MODE_ONE -> RepeatState.ONE
                Player.REPEAT_MODE_ALL -> RepeatState.ALL
                else -> RepeatState.OFF
            }
        }
    }

    private lateinit var controllerFuture: ListenableFuture<MediaController>

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @UnstableApi
    fun initialize(
        context: Context,
        binder: MusicService.MusicBinder,
    ) {
        Timber.i("Initializing MediaControllerManager")
        controllerFuture =
            binder.service.getSession().token.let {
                MediaController
                    .Builder(context, it)
                    .buildAsync()
            }
        controllerFuture.addListener({
            try {
                controller = controllerFuture.get().apply {
                    playWhenReady = true
                    addListener(playerListener)
                }
                controller?.addListener(playerListener)
                controller?.let { _position.value = it.currentPosition }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }, MoreExecutors.directExecutor())
        withController {
            if (repeatMode == Player.REPEAT_MODE_OFF) {
                repeatMode = Player.REPEAT_MODE_ALL
            }
        }
    }


    private var controller: MediaController? = null
    private val _playerState = MutableStateFlow(PlayerState.STOPPED)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    private val _repeatState = MutableStateFlow(RepeatState.OFF)
    val repeatState: StateFlow<RepeatState> = _repeatState.asStateFlow()
    private val _shuffleState = MutableStateFlow(ShuffleState.OFF)
    val shuffleState: StateFlow<ShuffleState> = _shuffleState.asStateFlow()

    private val _playlist = MutableStateFlow<Pair<Long, List<Any>>?>(null)
    val playlist: StateFlow<Playlist<LocalSong>?> = combine(
        _playlist,
        songRepository.allSongs
    ) { playlistPair, allSongs ->
        val (playlistId, songIds) = playlistPair ?: return@combine null
        val songs = songIds.mapNotNull { id -> allSongs.firstOrNull { it.id == id } }
        Playlist(
            id = playlistId,
            name = "Unknown",
            songs = songs,
            thumbnailSource = ThumbnailSource.None
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    private val _currentSongId: MutableStateFlow<Any?> = MutableStateFlow(null)
    val currentSong: StateFlow<Song> = combine(
        _currentSongId,
        songRepository.allSongs,
    ) { currentSongId, songs ->
        songs.firstOrNull { it.id == currentSongId } ?: UnknownSong
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = UnknownSong
    )
    private val _position = MutableStateFlow(0L)
    val position: StateFlow<Long> = _position.asStateFlow()
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()


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


    fun dispose() {
//        controller?.removeListener(playerListener)
//        controller?.release()
//        controller = null
    }

    /*============Controller actions============*/
    fun play(song: Song) {
        withControllerPlay {
            setMediaItem(song.toMediaItem())
            prepare()
            _playlist.value = Pair(Random.nextLong(), listOf(song.id))
            _currentSongId.value = song.id
        }
    }

    fun playPlaylist(playlist: Playlist<Song>, index: Int) = withControllerPlay {
        if (playlist.songs.isEmpty()) return@withControllerPlay
        if (playlist.id == _playlist.value?.first) {
            if (index in playlist.songs.indices) {
                seekTo(index, 0L)
            }
        } else {
            pause()
            clearMediaItems()
            setMediaItems(playlist.songs.map { it.toMediaItem() })
            prepare()
            seekTo(index, 0L)
        }
        _playlist.value = Pair(playlist.id, playlist.songs.map { it.id })
        _currentSongId.value = playlist.songs.getOrNull(index)?.id
    }

    fun seekToSliderPosition(sliderPosition: Float) {
        val duration = controller?.duration ?: return
        if (duration <= 0) return

        val clamped = sliderPosition.coerceIn(0f, 1f)
        val newPosition = (clamped * duration).toLong()

        withController {
            seekTo(newPosition)
        }
    }


    fun playPreviousSong() = withControllerPlay {
        seekToPreviousMediaItem()
    }

    fun togglePlayPause() = withController {
        if (isPlaying) pause() else play()
    }

    fun playNextSong() = withControllerPlay {
        seekToNextMediaItem()
    }

    fun toggleLikedCurrentSong() {
        scope.launch {
            val currentSong = currentSong.value
            if (currentSong is LocalSong) {
                songRepository.toggleLike(currentSong)
            }
        }
    }

    fun toggleShuffle() = withController {
        shuffleModeEnabled = !shuffleModeEnabled
    }

    fun toggleRepeat() = withController {
        repeatMode = when (repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }
    }
}