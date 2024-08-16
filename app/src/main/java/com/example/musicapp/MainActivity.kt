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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.common.AppCommon
import com.example.musicapp.data.service.MusicService
import com.example.musicapp.domain.repository.SongFileRepository
import com.example.musicapp.ui.App
import com.example.musicapp.ui.AppViewModel
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.util.EventData
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mediaControllerManager: MediaControllerManager

    @Inject
    lateinit var songFileRepository: SongFileRepository

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
                val viewModel = hiltViewModel<AppViewModel>()
                App(viewModel)
            }
        }
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.READ_MEDIA_AUDIO] == true) {
                    CoroutineScope(Dispatchers.IO).launch {
                        songFileRepository.reload()
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        startMusicService()

        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            CoroutineScope(Dispatchers.IO).launch {
                songFileRepository.reload()
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
            songFileRepository.reload()
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