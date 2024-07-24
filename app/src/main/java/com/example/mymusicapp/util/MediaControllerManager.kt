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

@OptIn(UnstableApi::class)
object MediaControllerManager {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController

    val currentSong = mutableStateOf<Song>(Song("NO SONG FOUND", null, null, "NO ARTIST FOUND"))

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
        controller.addListener(object : Player.Listener {
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

    fun playNext() {
        controller.seekToNext()
        controller.play()
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

    fun play(sliderPosition: Float) {
        controller.seekTo((controller.duration * sliderPosition).toLong())
    }
}