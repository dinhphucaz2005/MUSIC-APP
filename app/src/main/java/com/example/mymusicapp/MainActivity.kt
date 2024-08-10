package com.example.mymusicapp

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.di.AppModule
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


    init {
        System.loadLibrary("mymusicapp")
    }

    @Inject
    lateinit var mediaControllerManager: MediaControllerManager

    external fun stringComparison(str1: String, str2: String): Boolean

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
            myMusicService?.updateSong()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMusicService()
        setContent {
            App()
        }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            //TODO("Not yet implemented")
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
                //TODO("Not yet implemented")
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