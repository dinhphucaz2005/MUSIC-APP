package com.example.musicapp

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.innertube.YouTube
import com.example.innertube.models.YouTubeLocale
import com.example.musicapp.constants.PREFERENCE_KEY_COOKIE
import com.example.musicapp.constants.PREFERENCE_KEY_VISITOR_DATA
import com.example.musicapp.extension.logYT
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        getLocale()
        sharedPreferences.getString(PREFERENCE_KEY_COOKIE, null)?.let {
            YouTube.cookie = it
        }
        sharedPreferences.getString(PREFERENCE_KEY_VISITOR_DATA, null)?.let {
            YouTube.visitorData = it
        }

        CoroutineScope(Dispatchers.IO).launch {
            val result = YouTube.home().getOrNull()
            println(result)
        }

    }

    private fun getLocale() {
        YouTube.locale = YouTubeLocale(
            gl = "VN",
            hl = "vi"
        )
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