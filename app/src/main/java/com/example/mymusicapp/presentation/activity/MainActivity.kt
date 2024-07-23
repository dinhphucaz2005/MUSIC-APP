package com.example.mymusicapp.presentation.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.example.mymusicapp.ui.screen.App
import com.example.mymusicapp.util.MediaControllerManager


@UnstableApi
class MainActivity : AppCompatActivity() {

    private var isBound = false
    private var myMusicService: MusicService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("Service is connected")
            val binder = service as MusicService.MyBinder
            myMusicService = binder.getService()
            AppModule.setMusicService(myMusicService!!)
            isBound = true
            val sessionToken = myMusicService!!.getSession().token
            MediaControllerManager.initController(sessionToken)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppModule.init(this@MainActivity)
        startMusicService()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            mainViewModel.songListLoaded.observe(this@MainActivity) {
                myMusicService?.loadData(mainViewModel.getSongs())
            }
            App()
        }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            //TODO("Not yet implemented")
        } else {
            requestPermission()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppCommon.REQUEST_CODE_PERMISSION && permissions[0] == Manifest.permission.READ_MEDIA_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
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

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}