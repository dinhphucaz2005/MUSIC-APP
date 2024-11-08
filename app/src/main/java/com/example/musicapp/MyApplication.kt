package com.example.musicapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(), ImageLoaderFactory {

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