package com.example.musicapp.other.presentation.ui.widget

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.media3.common.Player
import com.example.musicapp.extension.toBitmap

class MusicWidget : GlanceAppWidget() {

    companion object {
        private const val TAG = "MusicWidget"
        val TITLE_KEY = stringPreferencesKey("title")
        val IS_PLAYING = booleanPreferencesKey("is_playing")
        val REPEAT_MODE = intPreferencesKey("repeat_mode")
        val SHUFFLE_MODE = booleanPreferencesKey("shuffle_mode")
        val BITMAP_KEY = byteArrayPreferencesKey("bitmap")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.e(TAG, "provideGlance: $id")
        provideContent {
            val preferences = currentState<Preferences>()
            val title = preferences[TITLE_KEY] ?: "No title"
            val isPlaying = preferences[IS_PLAYING] ?: false
            val repeatMode = preferences[REPEAT_MODE] ?: Player.REPEAT_MODE_OFF
            val shuffleMode = preferences[SHUFFLE_MODE] ?: false
            val bitmap = preferences[BITMAP_KEY]?.toBitmap()
            Log.e(TAG, "provideGlance: $title")
            MusicWidgetContent(
                context, title,
                isPlaying = isPlaying,
                repeatMode = repeatMode,
                shuffleMode = shuffleMode,
                bitmap = bitmap
            )
        }
    }
}