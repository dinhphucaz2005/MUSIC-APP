package nd.phuc.musicapp

import android.app.Application
import nd.phuc.musicapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    appModule,
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