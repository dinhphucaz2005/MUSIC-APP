package com.example.mymusicapp.helper

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.example.mymusicapp.R
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.service.MusicService

@UnstableApi
object NotificationHelper {

    fun createNotificationChannel(): NotificationChannelCompat {
        return NotificationChannelCompat.Builder(
            AppCommon.CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW
        ).setName("Music").setDescription("Play music").build()
    }


    fun createNotification(
        context: Context, mediaSession: MediaLibraryService.MediaLibrarySession
    ): Notification {


        val changePlaylistIntent = Intent(context, MusicService::class.java).apply {
            action = "ACTION_CHANGE_PLAYLIST"
        }
        val changePlaylistPendingIntent = PendingIntent.getService(
            context,
            0,
            changePlaylistIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        return NotificationCompat.Builder(context, AppCommon.CHANNEL_ID).apply {
            setSmallIcon(R.drawable.music_folder_song_solid, 0)
            addAction(
                R.drawable.notification,
                "Change Playlist",
                changePlaylistPendingIntent
            )
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionCompatToken)
                    .setShowCancelButton(true)
                    .setShowActionsInCompactView(0, 1, 2, 3, 4)
            )
        }.build()
    }
}