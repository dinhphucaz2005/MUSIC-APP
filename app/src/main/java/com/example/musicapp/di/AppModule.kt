package com.example.musicapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.constants.PREF_NAME
import com.example.musicapp.music.data.FirebaseDataSource
import com.example.musicapp.music.data.LocalDataSource
import com.example.musicapp.music.data.RoomDataSource
import com.example.musicapp.music.data.repository.CloudRepositoryImpl
import com.example.musicapp.music.data.repository.SongRepositoryImpl
import com.example.musicapp.music.data.repository.UserRepositoryImpl
import com.example.musicapp.music.domain.repository.CloudRepository
import com.example.musicapp.music.domain.repository.SongRepository
import com.example.musicapp.music.domain.repository.UserRepository
import com.example.musicapp.service.MusicService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
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
    fun provideFileCache(context: Context): File {
        return File(context.getExternalFilesDir("Music"), "media")
    }

    @Provides
    @Singleton
    fun provideMusicService(): MusicService {
        return MusicService()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
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