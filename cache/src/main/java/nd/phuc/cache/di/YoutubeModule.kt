package nd.phuc.cache.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nd.phuc.cache.data.database.YoutubeDatabase
import nd.phuc.cache.data.repository.DefaultCacheRepository
import nd.phuc.cache.domain.CacheRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YoutubeModule {

    @Provides
    @Singleton
    fun provideYoutubeDatabase(@ApplicationContext context: Context): YoutubeDatabase {
        return Room.databaseBuilder(
            context,
            YoutubeDatabase::class.java,
            "youtube.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCacheRepository(database: YoutubeDatabase): CacheRepository {
        return DefaultCacheRepository(database.playlistDao())
    }


}