package nd.phuc.music

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession

enum class MusicServiceAction {
    SHUFFLE,
    REPEAT,
    PLAY_OR_PAUSE,
    NEXT,
    PREVIOUS,
    PLAY,
}

enum class ActionPlayParameter {
    FILE_PATH
}

abstract class MusicService(
) : MediaLibraryService() {


    companion object {
        const val TAG = "MusicService"


        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_NAME = "Music"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Play music"
        private const val NOTIFICATION_CHANNEL_ID_NAME = "music_channel"
        const val ACTION_PLAYER_STATE_CHANGED = "nd.phuc.music.PLAYER_STATE_CHANGED"
        const val ACTION_PLAYER_STATE_CHANGED_IS_PLAYING = "isPlaying"
        const val ACTION_PLAYER_STATE_CHANGED_TITLE = "title"
        const val ACTION_PLAYER_STATE_CHANGED_ARTIST = "artist"
        const val ACTION_PLAYER_STATE_CHANGED_DURATION = "duration"
        const val ACTION_PLAYER_STATE_CHANGED_POSITION = "position"
    }

    private fun sendPlayerStateBroadcast() {
        val intent = Intent(ACTION_PLAYER_STATE_CHANGED)
        intent.putExtra(ACTION_PLAYER_STATE_CHANGED_IS_PLAYING, player.isPlaying)
        intent.putExtra(ACTION_PLAYER_STATE_CHANGED_TITLE, player.mediaMetadata.title ?: "")
        intent.putExtra(ACTION_PLAYER_STATE_CHANGED_ARTIST, player.mediaMetadata.artist ?: "")
        intent.putExtra(ACTION_PLAYER_STATE_CHANGED_DURATION, player.duration)
        intent.putExtra(ACTION_PLAYER_STATE_CHANGED_POSITION, player.currentPosition)
        sendBroadcast(intent)
    }

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession
    private lateinit var notificationManager: NotificationManagerCompat


    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MusicService created")
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
                    sendPlayerStateBroadcast()
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    updateNotification()
                    updateMusicWidget()
                    sendPlayerStateBroadcast()
                }
            })
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }

        session =
            MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {})
                .build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(
                NOTIFICATION_CHANNEL_ID_NAME, NotificationManagerCompat.IMPORTANCE_LOW
            ).setName(NOTIFICATION_CHANNEL_NAME).setDescription(NOTIFICATION_CHANNEL_DESCRIPTION)
                .build()
        )
        updateNotification()
    }


    private fun updateMusicWidget() {
//        serviceScope.launch {
//            val glanceAppWidgetManager = GlanceAppWidgetManager(this@MusicService)
//
//            val glanceIds: List<GlanceId> =
//                glanceAppWidgetManager.getGlanceIds(MusicWidget::class.java)
//
//            if (glanceIds.isNotEmpty()) {
//                val glanceId = glanceIds[0]
//
//                var title = ""
//                var isPlaying = false
//                var repeatMode = Player.REPEAT_MODE_OFF
//                var isShuffle = false
//                var bitmap: ByteArray = byteArrayOf()
//
//                val job = withMainContext {
//                    title = player.mediaMetadata.title.toString()
//                    isPlaying = player.isPlaying
//                    repeatMode = player.repeatMode
//                    isShuffle = player.shuffleModeEnabled
//                    bitmap = player.mediaMetadata.artworkData ?: bitmap
//                }
//
//                job.join()

//                updateAppWidgetState(this@MusicService, glanceId) { preferences ->
//                    preferences[MusicWidget.TITLE_KEY] = title
//                    preferences[MusicWidget.IS_PLAYING] = isPlaying
//                    preferences[MusicWidget.REPEAT_MODE] = repeatMode
//                    preferences[MusicWidget.SHUFFLE_MODE] = isShuffle
//                    preferences[MusicWidget.BITMAP_KEY] = bitmap
//                    Log.d(TAG, "Updated Title: ${preferences[MusicWidget.TITLE_KEY]}")
//                }
//
//                Log.d(TAG, "updateMusicWidget: $glanceId")
//                MusicWidget().update(this@MusicService, glanceId)
//            }
//        }
    }


    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ${intent?.action.toString()}")
        super.onStartCommand(intent, flags, startId)

        intent?.let {
            val action = MusicServiceAction.entries.find { action ->
                action.name == it.action
            } ?: return@let
            when (action) {
                MusicServiceAction.SHUFFLE -> {
                    player.shuffleModeEnabled = !player.shuffleModeEnabled
                }

                MusicServiceAction.REPEAT -> {
                    player.repeatMode = (player.repeatMode + 1) % 3
                }

                MusicServiceAction.PLAY_OR_PAUSE -> {
                    if (player.isPlaying) player.pause() else player.play()
                }

                MusicServiceAction.NEXT -> {
                    player.seekToNext()
                }

                MusicServiceAction.PREVIOUS -> {
                    player.seekToPrevious()
                }

                MusicServiceAction.PLAY -> {
                    val filePath = it.getStringExtra(ActionPlayParameter.FILE_PATH.name)
                    filePath?.let { path ->
                        val mediaItem = MediaItem.fromUri(path)
                        player.setMediaItem(mediaItem)
                        player.prepare()
                        player.play()
                    }
                }
            }
        }

        updateNotification()
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        session

    fun getSession(): MediaLibrarySession = session

    abstract val activity: Class<out Activity>

    @get:DrawableRes
    abstract val drawable: Int

    @SuppressLint("UnsafeOptInUsageError")
    private fun updateNotification() {
        val intent = Intent(this@MusicService, activity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this@MusicService, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        startForeground(
            NOTIFICATION_ID,
            NotificationCompat.Builder(this@MusicService, NOTIFICATION_CHANNEL_ID_NAME).apply {
                setSmallIcon(drawable)
                setContentIntent(pendingIntent)
                setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(session.sessionCompatToken).setShowCancelButton(true)
                )
            }.build()
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            player.run {
                stop()
                clearMediaItems()
                release()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error releasing player: ${e.message}")
        }
        session.release()
        notificationManager.cancel(NOTIFICATION_ID)
    }

}