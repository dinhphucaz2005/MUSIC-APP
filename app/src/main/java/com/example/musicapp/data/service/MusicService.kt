package com.example.musicapp.data.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.helper.NotificationHelper
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MusicService : MediaLibraryService() {

    @Inject
    lateinit var repository: PlaylistRepository

    companion object {
        private const val TAG = "MusicService"
    }

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession
    private lateinit var notificationManager: NotificationManagerCompat


    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        player.apply {
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    updateNotification()
                }
            })
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }

        coroutineScope.launch {
            repository.observeCurrentPlaylist().collect {
                if (it == null || it.songs.isEmpty())
                    return@collect
                CoroutineScope(Dispatchers.Main).launch {
                    player.pause()
                    player.clearMediaItems()
                    it.songs.forEach { song -> loadMediaItem(song) }
                    player.prepare()
                    it.currentSong?.let { index ->
                        player.seekTo(index, 0)
                    }
                    player.play()
                }
            }
        }

        session = MediaLibrarySession.Builder(
            this,
            player,
            object : MediaLibrarySession.Callback {
                override fun onGetLibraryRoot(
                    session: MediaLibrarySession,
                    browser: MediaSession.ControllerInfo,
                    params: LibraryParams?
                ): ListenableFuture<LibraryResult<MediaItem>> {
                    return super.onGetLibraryRoot(session, browser, params)
                }
            }
        ).build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(NotificationHelper.createNotificationChannel())
    }

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        updateNotification()
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        session

    fun getSession(): MediaLibrarySession = session

    private fun loadMediaItem(song: Song) {
        song.apply {
            val mediaMetadata = MediaMetadata.Builder().apply {
                setTitle(title)
                setArtist(author)
            }.build()
            val mediaItem = MediaItem.Builder().apply {
                setUri(uri)
                setMediaMetadata(mediaMetadata)
            }.build()
            player.addMediaItem(mediaItem)
        }
    }

    @ExperimentalFoundationApi
    private fun updateNotification() {
        startForeground(
            NotificationHelper.NOTIFICATION_ID,
            NotificationHelper.createNotification(this, session)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        player.clearMediaItems()
        player.release()
        session.release()
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
    }
}