package nd.phuc.musicapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import nd.phuc.core.extension.Route
import nd.phuc.core.helper.MediaControllerManager
import nd.phuc.core.presentation.theme.MyMusicAppTheme
import nd.phuc.core.service.MusicService
import javax.inject.Inject


@UnstableApi
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var mediaControllerManager: MediaControllerManager

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
                    val navController = rememberNavController()
                    AppScreen(
                        bottomNavigationBar = { modifier ->
                            AppNavigationBar(
                                modifier = modifier,
                                navigationItems = listOf(
                                    Screens.Home,
                                    Screens.Playlists,
                                    Screens.Library,
                                ),
                                navController = navController
                            )
                        }
                    ) {
                        AppNavGraph(navController = navController)
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


@Immutable
sealed class Screens : Route() {
    override val route: String = javaClass.simpleName

    data object Home : Screens()
    data object Playlists : Screens()
    data object Library : Screens()
}
