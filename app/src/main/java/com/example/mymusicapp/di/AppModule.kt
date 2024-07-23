package com.example.mymusicapp.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.data.repository.SongFileRepositoryImpl
import com.example.mymusicapp.data.service.MusicService

@UnstableApi
object AppModule {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

    fun provideAppContext(): Context = appContext

    private val songFileRepositoryImpl: SongFileRepositoryImpl by lazy {
        SongFileRepositoryImpl(appContext)
    }

    fun provideSongFileRepository(): SongFileRepositoryImpl {
        return songFileRepositoryImpl
    }

    private var musicService: MusicService? = null

    fun provideMusicService(): MusicService? = musicService

    fun setMusicService(myMusicService: MusicService) {
        this.musicService = myMusicService
    }


}