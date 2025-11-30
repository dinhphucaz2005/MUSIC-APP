package nd.phuc.musicapp

import android.app.Activity
import nd.phuc.music.MusicService

class AppMusicService : MusicService() {
    override val activity: Class<out Activity>
        get() = MainActivity::class.java

    override val drawable: Int
        get() = R.drawable.audio
}