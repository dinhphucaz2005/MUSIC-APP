package nd.phuc.musicapp

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import nd.phuc.music.MediaControllerManager
import timber.log.Timber
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import nd.phuc.music.MusicPlugin
import nd.phuc.music.MusicService


class MainActivity : FlutterFragmentActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MusicPlugin.registerWith(AppMusicService::class.java)
    }


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.i("Service connected")
            val binder = service as MusicService.MusicBinder
            MediaControllerManager.initialize(
                context = this@MainActivity,
                binder = binder,
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.i("Service disconnected")
            MediaControllerManager.dispose()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.i("Permission granted")
                /*
                                lifecycleScope.launch {
                                    songRepository.getSongs()
                                }
                */
                startMusicService()
            } else {
                Timber.i("Permission denied")
            }
        }


//        setContent {
//            MyMusicAppTheme {
//                CompositionLocalProvider(
//                    LocalMediaControllerManager provides mediaControllerManager,
//                ) {
//                    val backStack = rememberNavBackStack(Screens.Home)
//                    AppScreen(
//                        bottomNavigationBar = { modifier ->
//                            AppNavigationBar(
//                                modifier = modifier, navigationItems = listOf(
//                                    Screens.Home,
//                                    Screens.Playlists,
//                                    Screens.Library,
//                                ), backStack = backStack
//                            )
//                        }) {
//                        AppNavGraph(backStack = backStack, onNavigate = { screen ->
//                            backStack.add(screen)
//                        }, onBack = {
//                            if (backStack.size > 1) {
//                                backStack.removeAt(backStack.lastIndex)
//                            }
//                        })
//                    }
//                }
//            }
//        }
    }


    override fun onStart() {
        super.onStart()
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

        if (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            lifecycleScope.launch {
//                songRepository.getSongs()
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
        MediaControllerManager.dispose()
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
