package com.example.mymusicapp

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.util.EventData
import com.example.mymusicapp.util.MediaControllerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var mediaControllerManager: MediaControllerManager


    private var isBound = false
    private var myMusicService: MusicService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("Service is connected")
            val binder = service as MusicService.MyBinder
            myMusicService = binder.getService()
            isBound = true
            val sessionToken = myMusicService!!.getSession().token
            mediaControllerManager.initController(sessionToken)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    @Inject
    lateinit var songFileRepository: SongFileRepository

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        startMusicService()
        setContent {
            val viewModel = hiltViewModel<AppViewModel>()
            App(viewModel)
        }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            CoroutineScope(Dispatchers.IO).launch {
                songFileRepository.reload()
            }
        } else {
            requestPermission()
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
        unbindService(serviceConnection)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onMessageEvent(event: EventData) {
        Toast.makeText(this@MainActivity, event.message, Toast.LENGTH_SHORT).show()
        EventBus.getDefault().removeStickyEvent(event)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppCommon.REQUEST_CODE_PERMISSION && permissions[0] == Manifest.permission.READ_MEDIA_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Permission granted")
                CoroutineScope(Dispatchers.IO).launch {
                    songFileRepository.reload()
                }
            } else {
                println("Permission denied")
            }
        }
    }


    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        val serviceIntent = Intent(this@MainActivity, MusicService::class.java)
        startService(serviceIntent)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS,
                ), AppCommon.REQUEST_CODE_PERMISSION
            )
        }
    }
}