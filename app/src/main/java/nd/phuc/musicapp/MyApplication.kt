package nd.phuc.musicapp

import android.app.Application
import nd.phuc.core.database.di.databaseModule
import nd.phuc.core.domain.repository.di.repositoryModule
import nd.phuc.musicapp.di.appModule
import nd.phuc.musicapp.music.di.musicModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // In logcat
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    appModule,
                    databaseModule,
                    repositoryModule,
                    musicModule,
                )
            )
        }
//        CustomYoutube.locale = YouTubeLocale(
//            gl = "VN", hl = "en"
//        )
//        CustomYoutube.cookie = sharedPreferences.getString(PREFERENCE_KEY_COOKIE, null)
//        sharedPreferences.getString(PREFERENCE_KEY_VISITOR_DATA, null)?.let {
//            CustomYoutube.visitorData = it
//        }
    }
}