package com.example.musicapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.media3.common.util.UnstableApi
import androidx.room.Room
import com.example.musicapp.common.AppCommon
import com.example.musicapp.data.database.AppDatabase
import com.example.musicapp.data.repository.EditSongRepositoryImpl
import com.example.musicapp.data.repository.PlaylistRepositoryImpl
import com.example.musicapp.data.repository.SongRepositoryImpl
import com.example.musicapp.data.repository.UserRepositoryImpl
import com.example.musicapp.data.service.MusicService
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.domain.repository.SongFileRepository
import com.example.musicapp.domain.repository.UserRepository
import com.example.musicapp.util.MediaControllerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@UnstableApi
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppCommon.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideMusicService(): MusicService {
        return MusicService()
    }

    @Provides
    @Singleton
    fun provideSongFileRepository(context: Context): SongFileRepository {
        return SongRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun providePlaylistRepository(context: Context): PlaylistRepository {
        return PlaylistRepositoryImpl(provideRoomDatabase(context))
    }

    @Provides
    @Singleton
    fun provideEditSongRepository(context: Context): EditSongRepository {
        return EditSongRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideMediaControllerManager(
        context: Context,
    ): MediaControllerManager {
        return MediaControllerManager(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(AppCommon.PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository {
        return UserRepositoryImpl(sharedPreferences)
    }
}