package com.example.musicapp

import android.app.Application
import android.content.SharedPreferences
import com.example.innertube.CustomYoutube
import com.example.innertube.models.YouTubeLocale
import com.example.musicapp.constants.PREFERENCE_KEY_COOKIE
import com.example.musicapp.constants.PREFERENCE_KEY_VISITOR_DATA
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        CustomYoutube.locale = YouTubeLocale(
            gl = "VN", hl = "en"
        )
        CustomYoutube.cookie = sharedPreferences.getString(PREFERENCE_KEY_COOKIE, null)
        sharedPreferences.getString(PREFERENCE_KEY_VISITOR_DATA, null)?.let {
            CustomYoutube.visitorData = it
        }
    }
}