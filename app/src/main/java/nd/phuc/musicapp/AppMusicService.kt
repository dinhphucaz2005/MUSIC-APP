package nd.phuc.musicapp

import android.app.Activity
import dagger.hilt.android.AndroidEntryPoint
import nd.phuc.core.service.CustomMediaSourceFactory
import nd.phuc.core.service.MusicService
import javax.inject.Inject

@androidx.media3.common.util.UnstableApi
@AndroidEntryPoint
open class AppMusicService() : MusicService() {
    override val activity: Class<out Activity> = MainActivity::class.java
    override val drawable: Int = R.drawable.icon

    @Inject
    override lateinit var customMediaSourceFactory: CustomMediaSourceFactory
}
