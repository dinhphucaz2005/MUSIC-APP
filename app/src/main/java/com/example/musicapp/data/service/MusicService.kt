package com.example.musicapp.data.service

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.musicapp.domain.repository.SongFileRepository
import com.example.musicapp.helper.NotificationHelper
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MusicService : MediaLibraryService() {

    @Inject
    lateinit var songRepository: SongFileRepository

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

        Log.d(TAG, "onCreate: $TAG is created")
        CoroutineScope(Dispatchers.IO).launch {
            songRepository.getLocal().collect { songs ->
                if (songs.isEmpty()) return@collect
                CoroutineScope(Dispatchers.Main).launch {
                    player.pause()
                    player.clearMediaItems()
                    songs.forEach { song -> song.uri?.let { loadMediaItem(it) } }
                    player.prepare()
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
                    Log.d(TAG, "onGetLibraryRoot: ")
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        updateNotification()
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        session


    fun getSession(): MediaLibrarySession = session

    private fun loadMediaItem(uri: Uri) {
        player.addMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    private fun updateNotification() {
        startForeground(
            NotificationHelper.NOTIFICATION_ID, NotificationHelper.createNotification(this, session)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        player.clearMediaItems()
        player.release()
        session.release()
    }
}