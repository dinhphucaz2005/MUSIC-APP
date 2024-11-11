package com.example.musicapp

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.innertube.YouTube
import com.example.musicapp.extension.logYT
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.search(query = "Moonlight Shadow", filter = YouTube.SearchFilter.FILTER_VIDEO)
                .onSuccess {
                    logYT("$it")
                }
                .onFailure {
                    logYT(it.message.toString())
                }
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