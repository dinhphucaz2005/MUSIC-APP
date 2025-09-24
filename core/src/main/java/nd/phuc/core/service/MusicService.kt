package nd.phuc.core.service

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.C
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

@UnstableApi
abstract class MusicService(
) : MediaLibraryService() {

//    open lateinit var customMediaSourceFactory: CustomMediaSourceFactory

    companion object {
        const val ACTION_SHUFFLE = "ACTION_SHUFFLE"
        const val ACTION_REPEAT = "ACTION_REPEAT"
        const val ACTION_PLAY_OR_PAUSE = "ACTION_PLAY"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"

        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_NAME = "Music"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Play music"
        private const val NOTIFICATION_CHANNEL_ID_NAME = "MUSIC CHANNEL"
    }

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession
    private lateinit var notificationManager: NotificationManagerCompat


    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

//    private val _currentSongFlow = MutableStateFlow(Song.unidentifiedSong())
//    val currentSongFlow: StateFlow<Song> = _currentSongFlow.asStateFlow()

//    private fun updateCurrentSong(index: Int) {
//        serviceScope.launch {
//            val song = queueFlow.value?.songs?.getOrNull(index) ?: Song.unidentifiedSong()
//            _currentSongFlow.emit(song)
//        }
//    }


    private val _audioSessionId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val audioSessionId: StateFlow<Int?> = _audioSessionId.asStateFlow()


    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer
            .Builder(this)
//            .setMediaSourceFactory(
//                customMediaSourceFactory.getMediaSourceFactory()
//            )
            .build()

        val sessionId = player.audioSessionId

        if (sessionId != C.AUDIO_SESSION_ID_UNSET) _audioSessionId.value = sessionId

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
//                    updateCurrentSong(player.currentMediaItemIndex)
                }
            })
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }

        session = MediaLibrarySession
            .Builder(this, player, object : MediaLibrarySession.Callback {})
            .build()

        notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(
                NOTIFICATION_CHANNEL_ID_NAME,
                NotificationManagerCompat.IMPORTANCE_LOW
            ).setName(NOTIFICATION_CHANNEL_NAME)
                .setDescription(NOTIFICATION_CHANNEL_DESCRIPTION)
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
        Timber.d("onStartCommand: ${intent?.action.toString()}")
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

    abstract val activity: Class<out Activity>

    @get:DrawableRes
    abstract val drawable: Int

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
                        .setMediaSession(session.sessionCompatToken)
                        .setShowCancelButton(true)
                )
            }.build()
        )
    }


    override fun onDestroy() {
        super.onDestroy()
//        player.run {
//            clearMediaItems()
//            release()
//        }
        session.release()
//        notificationManager.cancel(NOTIFICATION_ID)
    }

}