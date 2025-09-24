package nd.phuc.musicapp.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import nd.phuc.core.service.CustomMediaSourceFactory
import nd.phuc.musicapp.AppMusicService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File


@SuppressLint("UnsafeOptInUsageError")
val appModule = module {

    single<File> { File(androidContext().getExternalFilesDir("Music"), "media") }

//    single<CustomMediaSourceFactory> { CustomMediaSourceFactory.getInstance(androidContext()) }
    single<AppMusicService> { AppMusicService() }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "music_app_pref",
            Context.MODE_PRIVATE
        )
    }

    single<SharedPreferences.Editor> { get<SharedPreferences>().edit() }
}
