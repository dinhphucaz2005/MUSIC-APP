package com.example.mymusicapp.data.service

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
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.helper.NotificationHelper
import com.example.mymusicapp.util.MediaControllerManager
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
    private var currentPlayList = AppCommon.INVALID_VALUE


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
            object : MediaLibrarySession.Callback {}
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
            AppCommon.NOTIFICATION_ID, NotificationHelper.createNotification(this, session)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPlayList = AppCommon.INVALID_VALUE
        player.clearMediaItems()
        player.release()
        session.release()
    }
}