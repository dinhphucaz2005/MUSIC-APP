package nd.phuc.musicapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.util.UnstableApi
import androidx.navigation3.runtime.rememberNavBackStack
import nd.phuc.core.helper.MediaControllerManager
import nd.phuc.core.presentation.theme.MyMusicAppTheme
import nd.phuc.core.service.MusicService
import org.koin.android.ext.android.get


@UnstableApi
class MainActivity : FragmentActivity() {

    var mediaControllerManager: MediaControllerManager =
        MediaControllerManager(songRepository = get())

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            mediaControllerManager.initialize(
                context = this@MainActivity,
                binder = binder,
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaControllerManager.dispose()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMusicService()
        setContent {
            MyMusicAppTheme {
                CompositionLocalProvider(
                    LocalMediaControllerManager provides mediaControllerManager,
                ) {
                    val backStack = rememberNavBackStack(Screens.Home)
                    AppScreen(
                        bottomNavigationBar = { modifier ->
                            AppNavigationBar(
                                modifier = modifier,
                                navigationItems = listOf(
                                    Screens.Home,
                                    Screens.Playlists,
                                    Screens.Library,
                                ),
                                backStack = backStack
                            )
                        }
                    ) {
                        AppNavGraph(backStack = backStack)
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        startService(
            Intent(
                this, MusicService::class.java
            )
        )
        bindService(
            Intent(this, AppMusicService::class.java), serviceConnection, BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaControllerManager.dispose()
    }


    private fun startMusicService() {
        val musicServiceIntent = Intent(
            this@MainActivity, MusicService::class.java
        )
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        startService(musicServiceIntent)
    }


}


