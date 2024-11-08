package com.example.musicapp.data.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.musicapp.helper.NotificationHelper
import com.example.musicapp.ui.widget.MusicWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MusicService : MediaLibraryService() {


    companion object {
        const val ACTION_SHUFFLE = "ACTION_SHUFFLE"
        const val ACTION_REPEAT = "ACTION_REPEAT"
        const val ACTION_PLAY_OR_PAUSE = "ACTION_PLAY"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
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

    private val backgroundScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        player.apply {
            playWhenReady = true
            addListener(object : Player.Listener {

                override fun onRepeatModeChanged(repeatMode: Int) {
                    super.onRepeatModeChanged(repeatMode)
                    updateMusicWidget()
                }

                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    super.onShuffleModeEnabledChanged(shuffleModeEnabled)
                    updateMusicWidget()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    updateMusicWidget()
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    updateNotification()
                    updateMusicWidget()
                }
            })
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }

        session =
            MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {})
                .build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(NotificationHelper.createNotificationChannel())
    }

    private fun updateMusicWidget() {
        backgroundScope.launch {
            val glanceAppWidgetManager = GlanceAppWidgetManager(this@MusicService)

            val glanceIds: List<GlanceId> =
                glanceAppWidgetManager.getGlanceIds(MusicWidget::class.java)

            if (glanceIds.isNotEmpty()) {
                val glanceId = glanceIds[0]

                var title = ""
                var isPlaying = false
                var repeatMode = Player.REPEAT_MODE_OFF
                var isShuffle = false
                var bitmap: ByteArray = byteArrayOf()

                val job = CoroutineScope(Dispatchers.Main).launch {
                    title = player.mediaMetadata.title.toString()
                    isPlaying = player.isPlaying
                    repeatMode = player.repeatMode
                    isShuffle = player.shuffleModeEnabled
                    bitmap = player.mediaMetadata.artworkData ?: bitmap
                }

                job.join()

                updateAppWidgetState(this@MusicService, glanceId) { preferences ->
                    preferences[MusicWidget.TITLE_KEY] = title
                    preferences[MusicWidget.IS_PLAYING] = isPlaying
                    preferences[MusicWidget.REPEAT_MODE] = repeatMode
                    preferences[MusicWidget.SHUFFLE_MODE] = isShuffle
                    preferences[MusicWidget.BITMAP_KEY] = bitmap
                    Log.d(TAG, "Updated Title: ${preferences[MusicWidget.TITLE_KEY]}")
                }

                Log.d(TAG, "updateMusicWidget: $glanceId")
                MusicWidget().update(this@MusicService, glanceId)
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action.toString()}")
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            when (it.action) {
                ACTION_SHUFFLE -> {
                    player.shuffleModeEnabled = !player.shuffleModeEnabled
                }

                ACTION_REPEAT -> {
                    player.repeatMode = (player.repeatMode + 1) % 3
                }

                ACTION_PLAY_OR_PAUSE -> {
                    if (player.isPlaying) player.pause() else player.play()
                }

                ACTION_NEXT -> {
                    player.seekToNext()
                }

                ACTION_PREVIOUS -> {
                    player.seekToPrevious()
                }
            }

        }

        updateNotification()
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        session

    fun getSession(): MediaLibrarySession = session

    private fun updateNotification() {
        startForeground(
            NotificationHelper.NOTIFICATION_ID,
            NotificationHelper.createNotification(this, session)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundScope.cancel()
        player.clearMediaItems()
        player.release()
        session.release()
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
    }
}