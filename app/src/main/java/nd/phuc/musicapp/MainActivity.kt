package nd.phuc.musicapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.coroutines.launch
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import nd.phuc.core.helper.MediaControllerManager
import nd.phuc.core.presentation.theme.MyMusicAppTheme
import nd.phuc.core.service.MusicService
import org.koin.android.ext.android.get
import timber.log.Timber
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


@UnstableApi
class MainActivity : FragmentActivity() {

    var mediaControllerManager: MediaControllerManager =
        MediaControllerManager(songRepository = get())

    private val songRepository: LocalSongRepository = get()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.i("Service connected")
            val binder = service as MusicService.MusicBinder
            mediaControllerManager.initialize(
                context = this@MainActivity,
                binder = binder,
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.i("Service disconnected")
            mediaControllerManager.dispose()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.i("Permission granted")
                lifecycleScope.launch {
                    songRepository.getSongs()
                }
                startMusicService()
            } else {
                Timber.i("Permission denied")
            }
        }


        setContent {
            MyMusicAppTheme {
                CompositionLocalProvider(
                    LocalMediaControllerManager provides mediaControllerManager,
                ) {
                    val backStack = rememberNavBackStack(Screens.Home)
                    AppScreen(
                        bottomNavigationBar = { modifier ->
                            AppNavigationBar(
                                modifier = modifier, navigationItems = listOf(
                                    Screens.Home,
                                    Screens.Playlists,
                                    Screens.Library,
                                ), backStack = backStack
                            )
                        }) {
                        AppNavGraph(backStack = backStack, onNavigate = { screen ->
                            backStack.add(screen)
                        }, onBack = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        })
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val permission =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                android.Manifest.permission.READ_MEDIA_AUDIO
            } else {
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }

        if (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            lifecycleScope.launch {
                songRepository.getSongs()
            }
            startMusicService()
        } else {
            requestPermissionLauncher.launch(permission)
        }
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
        Timber.i("Starting Music Service")
        val musicServiceIntent = Intent(
            this@MainActivity, AppMusicService::class.java
        )
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        startService(musicServiceIntent)
    }


}
