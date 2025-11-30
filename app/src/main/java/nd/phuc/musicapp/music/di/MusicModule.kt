package nd.phuc.musicapp.music.di

import nd.phuc.core.service.music.CustomMediaSourceFactory
import nd.phuc.musicapp.music.viewmodel.HomeViewModel
import nd.phuc.musicapp.music.viewmodel.PlaylistsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val musicModule = module {
    single<CustomMediaSourceFactory> { CustomMediaSourceFactory(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
    viewModel<PlaylistsViewModel> { PlaylistsViewModel(songRepository = get()) }
}
