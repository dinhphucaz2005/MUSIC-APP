package com.example.musicapp.helper

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.example.musicapp.R

@UnstableApi
object NotificationHelper {

    private const val NAME = "Music"
    private const val DESCRIPTION = "Play music"
    private const val ID = "MUSIC CHANNEL"
    const val NOTIFICATION_ID = 1

    fun createNotificationChannel(): NotificationChannelCompat {
        return NotificationChannelCompat.Builder(
            ID, NotificationManagerCompat.IMPORTANCE_LOW
        ).setName(NAME).setDescription(DESCRIPTION).build()
    }


    fun createNotification(
        context: Context,
        mediaSession: MediaLibraryService.MediaLibrarySession,
    ): Notification {
        return NotificationCompat.Builder(context, ID).apply {
            setSmallIcon(R.drawable.ic_disc)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionCompatToken)
                    .setShowCancelButton(true)
            )
        }.build()
    }
}