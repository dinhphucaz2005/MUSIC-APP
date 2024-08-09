package com.example.mymusicapp.util

import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.enums.PlaylistState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors




@OptIn(UnstableApi::class)
object MediaControllerManager {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController

    private var songList = ArrayList<Song>()

    fun getSong(): Song = songList[controller.currentMediaItemIndex]

    var isPlayingState = mutableStateOf<Boolean?>(null)

    val playListState = mutableStateOf(PlaylistState.REPEAT_ALL)
    val currentSong = mutableStateOf(Song())

    fun initController(
        sessionToken: SessionToken,
        songs: List<Song> = emptyList()
    ) {
        songList.addAll(songs)
        controllerFuture =
            MediaController.Builder(AppModule.provideAppContext(), sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                controller = controllerFuture.get()
            },
            MoreExecutors.directExecutor()
        )
        initController()
    }

    private fun initController() {
        controller.playWhenReady = true
        if (controller.shuffleModeEnabled)
            playListState.value = PlaylistState.SHUFFLE
        else if (controller.repeatMode == Player.REPEAT_MODE_ONE)
            playListState.value = PlaylistState.REPEAT_ONE
        else {
            playListState.value = PlaylistState.REPEAT_ALL
            controller.repeatMode = Player.REPEAT_MODE_ALL
        }
        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                isPlayingState.value = isPlaying
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                AppModule.provideMusicService().updateNotification()
                currentSong.value = songList[controller.currentMediaItemIndex]
            }
        })
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

    fun changePlayListState() {
        // SHUFFLE -> REPEAT_ALL -> REPEAT_ONE
        when (playListState.value) {
            PlaylistState.SHUFFLE -> {
                playListState.value = PlaylistState.REPEAT_ALL
                controller.repeatMode = Player.REPEAT_MODE_ALL
                controller.shuffleModeEnabled = false
            }

            PlaylistState.REPEAT_ALL -> {
                playListState.value = PlaylistState.REPEAT_ONE
                controller.repeatMode = Player.REPEAT_MODE_ONE
            }

            PlaylistState.REPEAT_ONE -> {
                playListState.value = PlaylistState.SHUFFLE
                controller.shuffleModeEnabled = true
                controller.repeatMode = Player.REPEAT_MODE_ALL
            }
        }
    }

    fun getCurrentPosition(): Float {
        return controller.currentPosition.toFloat() / controller.duration
    }

    fun seekTo(sliderPosition: Float) {
        controller.seekTo((controller.duration * sliderPosition).toLong())
    }

    fun addSongs(songs: List<Song>) {
        this.songList = songs as ArrayList<Song>
    }

}