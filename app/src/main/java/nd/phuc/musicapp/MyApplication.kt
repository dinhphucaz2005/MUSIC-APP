package nd.phuc.musicapp

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // In logcat
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
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