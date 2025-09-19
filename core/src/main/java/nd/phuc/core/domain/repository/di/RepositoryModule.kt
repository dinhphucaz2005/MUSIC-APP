package nd.phuc.core.domain.repository.di

import androidx.media3.common.util.UnstableApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nd.phuc.core.database.PlaylistDao
import nd.phuc.core.database.SongDao
import nd.phuc.core.domain.LocalDataSource
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import nd.phuc.core.domain.repository.implementation.LocalSongRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@UnstableApi
internal object RepositoryModule {

    @Provides
    @Singleton
    fun provideSongRepository(
        songDao: SongDao,
        playlistDao: PlaylistDao,
        localDataSource: LocalDataSource,
    ): LocalSongRepository {
        return LocalSongRepositoryImpl(
            localDataSource = localDataSource,
            songDao = songDao,
            playlistDao = playlistDao
        )
    }

}