package com.example.mymusicapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors


enum class PlayListState {
    SHUFFLE,
    REPEAT_ALL,
    REPEAT_ONE
}

@OptIn(UnstableApi::class)
object MediaControllerManager {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController

    var isPlayingState = mutableStateOf<Boolean?>(null)

    val playListState = mutableStateOf(PlayListState.REPEAT_ALL)
    val currentSong = mutableStateOf(Song("NO SONG FOUND", null, null, "NO ARTIST FOUND"))

    fun initController(sessionToken: SessionToken) {
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
            playListState.value = PlayListState.SHUFFLE
        else if (controller.repeatMode == Player.REPEAT_MODE_ONE)
            playListState.value = PlayListState.REPEAT_ONE
        else {
            playListState.value = PlayListState.REPEAT_ALL
            controller.repeatMode = Player.REPEAT_MODE_ALL
        }
        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                isPlayingState.value = isPlaying
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                AppModule.provideMusicService()?.updateNotification()
                currentSong.value = Song(
                    mediaMetadata.title.toString(),
                    null,
                    getArtworkFromMetadata(mediaMetadata),
                    mediaMetadata.artist.toString()
                )
            }
        })
    }

    private fun getArtworkFromMetadata(mediaMetadata: MediaMetadata): Bitmap? {
        mediaMetadata.artworkData?.let { data ->
            return BitmapFactory.decodeByteArray(data, 0, data.size)
        }

        mediaMetadata.artworkUri?.let { uri ->
            try {
                return AppModule.provideAppContext().contentResolver.openInputStream(uri)
                    ?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
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
            PlayListState.SHUFFLE -> {
                playListState.value = PlayListState.REPEAT_ALL
                controller.repeatMode = Player.REPEAT_MODE_ALL
                controller.shuffleModeEnabled = false
            }

            PlayListState.REPEAT_ALL -> {
                playListState.value = PlayListState.REPEAT_ONE
                controller.repeatMode = Player.REPEAT_MODE_ONE
            }

            PlayListState.REPEAT_ONE -> {
                playListState.value = PlayListState.SHUFFLE
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

}