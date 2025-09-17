package nd.phuc.musicapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nd.phuc.core.model.Song
import nd.phuc.musicapp.service.MusicService


@SuppressLint("UnsafeOptInUsageError")
class MediaControllerManager : Player.Listener {

    private lateinit var controllerFuture: ListenableFuture<MediaController>

    companion object {
        const val TAG = "MediaControllerManager"
    }

    fun initialize(
        context: Context,
        binder: MusicService.MusicBinder,
    ) {
        controllerFuture =
            binder.service.getSession().token.let {
                MediaController
                    .Builder(context, it)
                    .buildAsync()
            }
        audioSessionId = binder.service.audioSessionId
        Log.d(TAG, "init")
        controllerFuture.addListener({
            try {
                controller = controllerFuture.get().apply {
                    playWhenReady = true
                    addListener(this@MediaControllerManager)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating MediaController", e)
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
    private val _repeatState = MutableStateFlow(RepeatState.NONE)
    val repeatState: StateFlow<RepeatState> = _repeatState.asStateFlow()
    private val _shuffleState = MutableStateFlow(ShuffleState.OFF)
    val shuffleState: StateFlow<ShuffleState> = _shuffleState.asStateFlow()
    private val _currentSong = MutableStateFlow(Song.unidentifiedSong())
    val currentSong: StateFlow<Song> = _currentSong.asStateFlow()
    var audioSessionId: StateFlow<Int?> = MutableStateFlow(null)
        private set


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
        controller?.removeListener(this)
    }

    fun play(song: Song) {
        withControllerPlay {
            setMediaItem(song.toMediaItem())
            _currentSong.value = song
        }
    }


    enum class PlayerState {
        PLAYING,
        PAUSED,
        STOPPED;

        companion object {
            fun fromBoolean(playing: Boolean): PlayerState {
                return if (playing) PLAYING else PAUSED
            }
        }
    }

    enum class RepeatState {
        NONE,
        ONE,
        ALL
    }

    enum class ShuffleState {
        OFF,
        ON
    }
}