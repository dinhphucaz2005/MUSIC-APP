package com.example.mymusicapp.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.navigation.ui.AppBarConfiguration
import androidx.room.Room
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.repository.SongFileRepositoryImpl
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.domain.repository.SongFileRepository

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

    fun provideSongFileRepository(): SongFileRepository {
        return songFileRepositoryImpl
    }

    private lateinit var musicService: MusicService

    fun provideMusicService(): MusicService {
        return musicService
    }

    fun initMusicService(musicService: MusicService) {
        this.musicService = musicService
    }


    private val roomDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java,
            AppCommon.DATABASE_NAME
        ).build()
    }

    fun provideRoomDatabase(): AppDatabase {
        return roomDatabase
    }

}