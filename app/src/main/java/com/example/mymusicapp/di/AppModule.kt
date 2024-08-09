package com.example.mymusicapp.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.room.Room
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.repository.PlaylistRepositoryImpl
import com.example.mymusicapp.data.repository.SongRepositoryImpl
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.domain.repository.PlaylistRepository
import com.example.mymusicapp.domain.repository.SongFileRepository

@UnstableApi
object AppModule {

    private lateinit var appContext: Context
    private lateinit var musicService: MusicService
    private val roomDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppCommon.DATABASE_NAME
        ).build()
    }

    fun init(context: Context) {
        appContext = context
    }

    fun provideAppContext(): Context {
        if (!::appContext.isInitialized) {
            throw IllegalStateException("AppContext not initialized")
        }
        return appContext
    }

    fun provideMusicService(): MusicService {
        if (!::musicService.isInitialized) {
            throw IllegalStateException("MusicService not initialized")
        }
        return musicService
    }

    fun initMusicService(musicService: MusicService) {
        this.musicService = musicService
    }

    fun provideSongFileRepository(): SongFileRepository {
        return SongRepositoryImpl(provideAppContext())
    }

    fun providePlaylistRepository(): PlaylistRepository {
        return PlaylistRepositoryImpl(provideRoomDatabase())
    }

    fun provideRoomDatabase(): AppDatabase {
        return roomDatabase
    }
}
