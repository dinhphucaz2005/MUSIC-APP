package nd.phuc.musicapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.media3.common.util.UnstableApi
import nd.phuc.core.model.PREF_NAME
import nd.phuc.musicapp.music.data.LocalDataSource
import nd.phuc.musicapp.music.data.RoomDataSource
import nd.phuc.musicapp.music.data.repository.LocalSongRepository
import nd.phuc.musicapp.music.data.repository.UserRepositoryImpl
import nd.phuc.musicapp.music.domain.repository.SongRepository
import nd.phuc.musicapp.music.domain.repository.UserRepository
import nd.phuc.musicapp.service.MusicService
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
    fun provideSongRepository(
        context: Context, roomDataSource: RoomDataSource, localDataSource: LocalDataSource
    ): SongRepository {
        return LocalSongRepository(context, roomDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository {
        return UserRepositoryImpl(sharedPreferences)
    }
}