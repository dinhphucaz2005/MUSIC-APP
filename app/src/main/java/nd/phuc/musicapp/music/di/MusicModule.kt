package nd.phuc.musicapp.music.di

import nd.phuc.musicapp.music.home.HomeViewModel
import nd.phuc.musicapp.music.playlists.PlaylistsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val musicModule = module {
    viewModel<HomeViewModel> { HomeViewModel(get()) }
    viewModel<PlaylistsViewModel> { PlaylistsViewModel(songRepository = get()) }
}
