package com.example.musicapp

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.data.service.MusicService
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.ui.Main
import com.example.musicapp.ui.MainViewModel
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.screen.song.SongScreen
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.util.EventData
import com.example.musicapp.util.MediaControllerManager
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject


@Suppress("DEPRECATION")
@OptIn(ExperimentalAnimationApi::class)
@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mediaControllerManager: MediaControllerManager

    @Inject
    lateinit var playlistRepository: PlaylistRepository

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>


    private var isBound = false
    private var myMusicService: MusicService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder
            myMusicService = binder.getService()
            isBound = true
            val sessionToken = myMusicService!!.getSession().token
            mediaControllerManager.initializeMediaController(sessionToken)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicTheme {
                val navController = rememberNavController()
                val mainViewModel = hiltViewModel<MainViewModel>()

                AnimatedNavHost(
                    navController = navController,
                    startDestination = Routes.OTHER.name
                ) {
                    composable(Routes.OTHER.name) {
                        Main(mainViewModel) { navController.navigate(Routes.SONG.name) }
                    }
                    composable(
                        route = Routes.SONG.name,
                        enterTransition = {
                            slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = EaseInOut
                                )
                            ) + scaleIn(
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = EaseInOut
                                )
                            )
                        },
                        exitTransition = {
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = EaseInOut
                                )
                            ) + scaleOut(
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = EaseInOut
                                )
                            )
                        }
                    ) {
                        SongScreen(navHostController = navController, viewModel = mainViewModel)
                    }
                }
            }
        }
        startMusicService()
        handlePermissions()
    }

    private fun handlePermissions() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.READ_MEDIA_AUDIO] == true) {
                    CoroutineScope(Dispatchers.IO).launch {
                        playlistRepository.reload()
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            CoroutineScope(Dispatchers.IO).launch {
                playlistRepository.reload()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        CoroutineScope(Dispatchers.IO).launch {
            playlistRepository.reload()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
        }
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onMessageEvent(event: EventData) {
        Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_SHORT).show()
        EventBus.getDefault().removeStickyEvent(event)
    }

    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        startService(musicServiceIntent)
    }
}
