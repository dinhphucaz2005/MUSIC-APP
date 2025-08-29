package nd.phuc.musicapp.helper

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.session.MediaLibraryService
import nd.phuc.musicapp.MainActivity
import nd.phuc.musicapp.R

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


    @SuppressLint("UnsafeOptInUsageError")
    fun createNotification(
        context: Context,
        mediaSession: MediaLibraryService.MediaLibrarySession,
    ): Notification {

        val intent = Intent(context, MainActivity::class.java).apply {
//            Intent.setFlags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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