package com.example.shared.service

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

@Suppress("DEPRECATION")
class TestService : MediaBrowserServiceCompat() {

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): TestService = this@TestService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    companion object {
        private const val TAG = "TestService"
        private const val BROWSER_ROOT_ID = "root"
        private const val SESSION_TAG = "My Music Service"
    }

    private lateinit var session: MediaSessionCompat
    private lateinit var player: ExoPlayer

    private var currentSongIndex = 0
    private val songList = emptyList<MediaItem>()

    private val callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            super.onPlay()
            playSong()
        }

        override fun onPause() {
            super.onPause()
            pauseSong()
        }

        override fun onStop() {
            super.onStop()
            stopSelf()
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            skipToNextSong()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            skipToPreviousSong()
        }
    }

    private fun playSong() {
        if (currentSongIndex < songList.size) {
            player.setMediaItem(songList[currentSongIndex])
            player.prepare()
            player.play()
            session.isActive = true
            session.setMetadata(createMetadata(currentSongIndex))
        }
    }

    private fun pauseSong() {
        player.pause()
    }

    private fun skipToNextSong() {
        if (currentSongIndex < songList.size - 1) {
            currentSongIndex++
            playSong()
        }
    }

    private fun skipToPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--
            playSong()
        }
    }

    private fun createMetadata(index: Int): MediaMetadataCompat {
        return MediaMetadataCompat.Builder()
            .putString(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                "Song Title ${index + 1}"
            )
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist Name")
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "Album Name")
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, player.duration)
            .build()
    }


    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(BROWSER_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(getMediaItems())
    }

    private fun getMediaItems(): MutableList<MediaBrowserCompat.MediaItem> {
        return mutableListOf()
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate: ")

        // Initial session
        session = MediaSessionCompat(this, SESSION_TAG).apply {
            setCallback(callback)
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
        }
        sessionToken = session.sessionToken
        // Initial Exo Player
        player = ExoPlayer.Builder(this).build()
        player.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        session.release()
        player.release()
    }


}