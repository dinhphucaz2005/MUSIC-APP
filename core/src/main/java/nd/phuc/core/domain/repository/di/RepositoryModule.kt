package nd.phuc.core.domain.repository.di

import nd.phuc.core.domain.LocalDataSource
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import nd.phuc.core.domain.repository.implementation.LocalSongRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<LocalDataSource> {
        LocalDataSource(
            context = get()
        )
    }

    single<LocalSongRepository> {
        LocalSongRepositoryImpl(
            localDataSource = get(),
            songDao = get(),
            playlistDao = get()
        )
    }

}
