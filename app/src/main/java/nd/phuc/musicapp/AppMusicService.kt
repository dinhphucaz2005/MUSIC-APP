package nd.phuc.musicapp

import android.app.Activity
import nd.phuc.core.service.MusicService
import timber.log.Timber

@androidx.media3.common.util.UnstableApi
open class AppMusicService() : MusicService() {
    override val activity: Class<out Activity> = MainActivity::class.java
    override val drawable: Int = R.drawable.icon

//    override var customMediaSourceFactory: CustomMediaSourceFactory = get()

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("On destroy")
    }
}
