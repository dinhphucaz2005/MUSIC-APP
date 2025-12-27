package nd.phuc.musicapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import timber.log.Timber


class MusicService : MediaLibraryService() {
    enum class MusicServiceAction {
        SHUFFLE, REPEAT, PLAY_OR_PAUSE, NEXT, PREVIOUS, PLAY,
    }

    enum class ActionPlayParameter {
        FILE_PATH
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_NAME = "Music"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Play music"
        private const val NOTIFICATION_CHANNEL_ID_NAME = "music_channel"
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
        Timber.d("MusicService created")
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


    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand: ${intent?.action.toString()}")
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

    @SuppressLint("UnsafeOptInUsageError")
    private fun updateNotification() {
        val intent = Intent(this@MusicService, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this@MusicService, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        startForeground(
            NOTIFICATION_ID,
            NotificationCompat.Builder(this@MusicService, NOTIFICATION_CHANNEL_ID_NAME).apply {
                setSmallIcon(R.drawable.audio)
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
            Timber.w("Error releasing player: ${e.message}")
        }
        session.release()
        notificationManager.cancel(NOTIFICATION_ID)
    }

}