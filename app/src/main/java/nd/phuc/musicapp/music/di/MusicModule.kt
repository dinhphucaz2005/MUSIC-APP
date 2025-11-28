package nd.phuc.musicapp.music.di

import nd.phuc.musicapp.music.viewmodel.HomeViewModel
import nd.phuc.musicapp.music.viewmodel.PlaylistsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val musicModule = module {
//    single<CustomMediaSourceFactory> { CustomMediaSourceFactory.getInstance(androidContext()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
    viewModel<PlaylistsViewModel> { PlaylistsViewModel(songRepository = get()) }
}
