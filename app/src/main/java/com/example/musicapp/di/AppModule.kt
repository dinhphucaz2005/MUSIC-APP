package com.example.musicapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.common.AppCommon
import com.example.musicapp.data.FirebaseDataSource
import com.example.musicapp.data.LocalDataSource
import com.example.musicapp.data.RoomDataSource
import com.example.musicapp.data.repository.CloudRepositoryImpl
import com.example.musicapp.data.repository.SongRepositoryImpl
import com.example.musicapp.data.repository.UserRepositoryImpl
import com.example.musicapp.data.service.MusicService
import com.example.musicapp.domain.repository.CloudRepository
import com.example.musicapp.domain.repository.SongRepository
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
    fun provideMediaControllerManager(
        context: Context, playlistRepository: SongRepository
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
    fun providePlaylistRepository(
        context: Context, roomDataSource: RoomDataSource, localDataSource: LocalDataSource
    ): SongRepository {
        return SongRepositoryImpl(context, roomDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository {
        return UserRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideCloudRepository(
        firebaseDataSource: FirebaseDataSource
    ): CloudRepository {
        return CloudRepositoryImpl(firebaseDataSource)
    }
}