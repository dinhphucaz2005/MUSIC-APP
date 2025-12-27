package nd.phuc.musicapp.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import nd.phuc.musicapp.MediaControllerManager
import nd.phuc.musicapp.data.repository.SongRepository
import nd.phuc.musicapp.service.music.CustomMediaSourceFactory
import nd.phuc.musicapp.ui.songs.SongsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.io.File


@SuppressLint("UnsafeOptInUsageError")
val appModule = module {

    single<File> { File(androidContext().getExternalFilesDir("Music"), "media") }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "music_app_pref", Context.MODE_PRIVATE
        )
    }

    single<SharedPreferences.Editor> { get<SharedPreferences>().edit() }

    single<CustomMediaSourceFactory> {
        CustomMediaSourceFactory(
            context = androidContext(),
        )
    }

    single { SongRepository() }
    single { MediaControllerManager() }

    viewModel { SongsViewModel(get()) }
}
