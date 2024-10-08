package com.example.musicapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.media3.common.util.UnstableApi
import androidx.room.Room
import com.example.musicapp.common.AppCommon
import com.example.musicapp.data.api.ApiService
import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.data.database.AppDatabase
import com.example.musicapp.data.repository.CloudRepositoryImpl
import com.example.musicapp.data.repository.EditSongRepositoryImpl
import com.example.musicapp.data.repository.PlaylistRepositoryImpl
import com.example.musicapp.data.repository.UploadRepositoryImpl
import com.example.musicapp.data.repository.UserRepositoryImpl
import com.example.musicapp.data.service.MusicService
import com.example.musicapp.domain.repository.CloudRepository
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.domain.repository.UploadRepository
import com.example.musicapp.domain.repository.UserRepository
import com.example.musicapp.util.MediaControllerManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
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
    fun provideDao(database: AppDatabase): AppDAO = database.appDAO()

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
    fun provideEditSongRepository(context: Context): EditSongRepository {
        return EditSongRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun providePlaylistRepository(
        context: Context,
        dao: AppDAO,
    ): PlaylistRepository {
        return PlaylistRepositoryImpl(context, dao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository {
        return UserRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideUploadRepository(
        storage: StorageReference,
        database: DatabaseReference
    ): UploadRepository {
        return UploadRepositoryImpl(storage, database)
    }

    @Provides
    @Singleton
    fun provideCloudRepository(
        apiService: ApiService
    ): CloudRepository {
        return CloudRepositoryImpl(apiService)
    }
}