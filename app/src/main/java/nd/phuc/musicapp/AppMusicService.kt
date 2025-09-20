package nd.phuc.musicapp

import android.app.Activity
import nd.phuc.core.service.CustomMediaSourceFactory
import nd.phuc.core.service.MusicService
import org.koin.android.ext.android.get

@androidx.media3.common.util.UnstableApi
open class AppMusicService() : MusicService() {
    override val activity: Class<out Activity> = MainActivity::class.java
    override val drawable: Int = R.drawable.icon

    override var customMediaSourceFactory: CustomMediaSourceFactory = get()
}
