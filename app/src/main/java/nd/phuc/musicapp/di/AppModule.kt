package nd.phuc.musicapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.UnstableApi
import dagger.hilt.components.SingletonComponent
import nd.phuc.core.service.CustomMediaSourceFactory
import nd.phuc.musicapp.AppMusicService
import java.io.File
import javax.inject.Singleton

const val PREF_NAME = "music_app_pref"

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

    @androidx.media3.common.util.UnstableApi
    @Provides
    @Singleton
    fun provideMusicService(
        customMediaSourceFactory: CustomMediaSourceFactory,
    ): AppMusicService {
        return AppMusicService()
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

}