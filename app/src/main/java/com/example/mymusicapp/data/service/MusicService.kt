package com.example.mymusicapp.data.service

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.helper.NotificationHelper
import com.example.mymusicapp.util.MediaControllerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@UnstableApi
class MusicService : MediaLibraryService() {

    private val songRepository = AppModule.provideSongFileRepository()

    companion object {
        private const val TAG = "MusicService"
        const val LOCAL_FILE = 0
        const val ROOM_DATABASE_FILE = 1
        const val FIREBASE_FILE = 2
        const val PLAY_LIST_INDEX = -1
    }

    private var currentType = -1

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
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }

        session = MediaLibrarySession.Builder(this, player, object : MediaLibrarySession.Callback {
        }).build()

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

    private fun loadData(
        songList: List<Song>,
        fileFromType: Int = LOCAL_FILE
    ) {
        player.pause()
        player.clearMediaItems()
        player.clearMediaItems()
        songList.forEach {
            if (it.uri != null) loadMediaItem(it.uri)
        }
        player.prepare()
        player.play()
    }

    private fun loadMediaItem(uri: Uri) {
        player.addMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    fun updateNotification() {
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

    fun updateSong() {
        CoroutineScope(Dispatchers.IO).launch {
            songRepository.getAllAudioFiles().collect {
                if (it.isEmpty()) return@collect
                MediaControllerManager.addSongs(it)
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d(TAG, "updateSong: ${it.size}")
                    loadData(it)
                }
            }
        }
    }
}