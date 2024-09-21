package com.example.musicapp.helper

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.example.musicapp.MainActivity
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


    @ExperimentalFoundationApi
    fun createNotification(
        context: Context,
        mediaSession: MediaLibraryService.MediaLibrarySession,
    ): Notification {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, ID).apply {
            setSmallIcon(R.drawable.ic_disc)
            setContentIntent(pendingIntent)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionCompatToken)
                    .setShowCancelButton(true)
            )
        }.build()
    }
}