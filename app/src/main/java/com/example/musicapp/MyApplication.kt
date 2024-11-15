package com.example.musicapp

import android.app.Application
import android.content.SharedPreferences
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.innertube.CustomYoutube
import com.example.innertube.models.YouTubeLocale
import com.example.musicapp.constants.PREFERENCE_KEY_COOKIE
import com.example.musicapp.constants.PREFERENCE_KEY_VISITOR_DATA
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), ImageLoaderFactory {

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

    override fun newImageLoader(): ImageLoader = ImageLoader(this).newBuilder().apply {
        memoryCachePolicy(CachePolicy.ENABLED)
        memoryCache {
            MemoryCache.Builder(this@MyApplication).apply {
                maxSizePercent(0.1)
                strongReferencesEnabled(true)
            }.build()
        }
        diskCachePolicy(CachePolicy.ENABLED)
        diskCache {
            DiskCache.Builder().apply {
                maxSizePercent(0.03)
                directory(cacheDir)
            }.build()
        }
        logger(DebugLogger())
    }.build()
}