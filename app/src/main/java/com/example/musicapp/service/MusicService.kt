package com.example.musicapp.service

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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.musicapp.extension.withIOContext
import com.example.musicapp.extension.withMainContext
import com.example.musicapp.helper.NotificationHelper
import com.example.musicapp.other.domain.model.FirebaseSong
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.YoutubeSong
import com.example.musicapp.other.domain.repository.SongRepository
import com.example.musicapp.other.presentation.ui.widget.MusicWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MusicService : MediaLibraryService() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)


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


    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    private val _queueFlow = MutableStateFlow<Queue?>(null)
    val queueFlow: StateFlow<Queue?> = _queueFlow.asStateFlow()

    fun updateQueue(queue: Queue) {
        serviceScope.launch {
            _queueFlow.emit(queue)
        }
    }

    private val _currentSongFlow = MutableStateFlow(Song.unidentifiedSong())
    val currentSongFlow: StateFlow<Song> = _currentSongFlow.asStateFlow()

    private fun updateCurrentSong(index: Int) {
        serviceScope.launch {
            val song = queueFlow.value?.songs?.getOrNull(index) ?: Song.unidentifiedSong()
            _currentSongFlow.emit(song)
        }
    }

    @Inject
    lateinit var customMediaSourceFactory: CustomMediaSourceFactory

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer
            .Builder(this)
            .setMediaSourceFactory(customMediaSourceFactory.getInstance())
            .build()

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
                    updateCurrentSong(player.currentMediaItemIndex)
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
        serviceScope.launch {
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

                val job = withMainContext {
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
        serviceJob.cancel()
        player.run {
            clearMediaItems()
            release()
        }
        session.release()
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
    }

    fun downloadCurrentSong() {
        val downloadService = Intent(this, DownloadService::class.java)
//        downloadService.putExtra(DownloadService.URL, getCurrentSong()?.getDownloadUrl())
//        downloadService.putExtra(DownloadService.FILE_NAME, getCurrentSong()?.getTitle())
        startService(downloadService)
    }

    @Inject
    lateinit var songRepository: SongRepository

    fun toggleLikedSong(song: Song) {
        withIOContext {
            if (song.isLiked) {
                songRepository.unlikeSong(song)
            } else {
                songRepository.likeSong(song)
            }
            if (song.getAudio() == currentSongFlow.value.getAudio()) { // if the current song is liked
                _currentSongFlow.emit(

                    when (song) {
                        is LocalSong -> song.copy(isLiked = !song.isLiked)
                        is FirebaseSong -> song.copy(isLiked = !song.isLiked)
                        is YoutubeSong -> song.copy(isLiked = !song.isLiked)
                        else -> song
                    }
                )
            }
        }
    }

    fun addToNext(song: Song) {
        player.addMediaItem(player.currentMediaItemIndex + 1, song.toMediaItem())
        _queueFlow.value?.let {
            updateQueue(it.copy(songs = it.songs.toMutableList().apply { add(it.index + 1, song) }))
        }
    }

    fun addToQueue(song: Song) {
        player.addMediaItem(song.toMediaItem())
        _queueFlow.value?.let {
            updateQueue(it.copy(songs = it.songs.toMutableList().apply { add(song) }))
        }
    }

}