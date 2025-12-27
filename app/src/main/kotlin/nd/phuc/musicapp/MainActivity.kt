package nd.phuc.musicapp

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import timber.log.Timber
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import nd.phuc.musicapp.navigation.AppNavGraph
import nd.phuc.musicapp.ui.main.AppNavigationBar
import nd.phuc.musicapp.ui.main.AppScreen
import org.koin.android.ext.android.inject
import nd.phuc.musicapp.ui.theme.MusicAppTheme


class MainActivity : ComponentActivity() {


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val mediaControllerManager: MediaControllerManager by inject()
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.i("Service connected")
            val binder = service as MusicService.MusicBinder
            mediaControllerManager.initialize(
                context = this@MainActivity,
                binder = binder
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.i("Service disconnected")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.i("Permission granted")
                startMusicService()
            } else {
                Timber.i("Permission denied")
            }
        }

        setContent {
            MusicAppTheme {
                CompositionLocalProvider(
                    LocalMediaControllerManager provides mediaControllerManager,
                ) {
                    val navController = rememberNavController()

                    AppScreen(
                        bottomNavigationBar = { modifier ->
                            AppNavigationBar(
                                modifier = modifier,
                                navigationItems = listOf(
                                    "home",
                                    "playlist",
                                    "artist",
                                ),
                                navController = navController
                            )
                        }) {
                        AppNavGraph(navController = navController)
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
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
    }


    private fun startMusicService() {
        Timber.i("Starting Music Service")
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        startService(musicServiceIntent)
    }


}
