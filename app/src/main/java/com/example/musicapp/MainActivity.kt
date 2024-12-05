package com.example.musicapp

import android.Manifest
import android.content.ComponentName
import android.content.Context
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.other.domain.repository.SongRepository
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.service.MusicService
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.youtube.presentation.YoutubeViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: SongRepository

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>


    private var mediaControllerManager by mutableStateOf<MediaControllerManager?>(null)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            mediaControllerManager =
                MediaControllerManager(this@MainActivity, binder, lifecycleScope)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaControllerManager?.dispose()
            mediaControllerManager = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMusicService()
        handlePermissions()
        setContent {
            MusicTheme {
                CompositionLocalProvider(
                    LocalMediaControllerManager provides mediaControllerManager
                ) {
                    App(hiltViewModel<HomeViewModel>(), hiltViewModel<YoutubeViewModel>())
                }
            }
        }
    }


    private fun handlePermissions() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.READ_MEDIA_AUDIO] == true) {
                    // TODO: load song from local storage
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            // TODO: load song from local storage
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this, MusicService::class.java))
        bindService(
            Intent(this, MusicService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaControllerManager = null
        EventBus.getDefault().unregister(this)
    }


    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        startService(musicServiceIntent)
    }
}

val LocalMediaControllerManager =
    staticCompositionLocalOf<MediaControllerManager?> { error("No PlayerConnection provided") }
