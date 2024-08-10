package com.example.mymusicapp.di

import android.app.Application
import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.room.Room
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.repository.EditSongRepositoryImpl
import com.example.mymusicapp.data.repository.PlaylistRepositoryImpl
import com.example.mymusicapp.data.repository.SongRepositoryImpl
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.domain.repository.EditSongRepository
import com.example.mymusicapp.domain.repository.PlaylistRepository
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.util.MediaControllerManager
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
}
