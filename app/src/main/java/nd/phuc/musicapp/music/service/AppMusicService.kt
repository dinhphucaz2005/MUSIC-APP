package nd.phuc.musicapp.music.service

import android.app.Activity
import androidx.media3.common.util.UnstableApi
import nd.phuc.core.service.MusicService
import nd.phuc.musicapp.MainActivity
import nd.phuc.musicapp.R
import timber.log.Timber

@UnstableApi
open class AppMusicService() : MusicService() {
    override val activity: Class<out Activity> = MainActivity::class.java
    override val drawable: Int = R.drawable.icon

//    override var customMediaSourceFactory: CustomMediaSourceFactory = get()

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("On destroy")
    }
}