package com.example.mymusicapp.presentation.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.callback.SongItemListener
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.dto.SongFileDTO
import com.example.mymusicapp.data.service.MusicService
import com.example.mymusicapp.databinding.ActivityPlayListBinding
import com.example.mymusicapp.presentation.adapter.SongAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel

@UnstableApi
class PlayListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayListBinding

    private var isBound = false

    private var myMusicService: MusicService? = null
    private lateinit var controller: MediaController

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("Service is connected")
            val binder = service as MusicService.MyBinder
            myMusicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
        prepareRecyclerView()
        dataBinding()
        connectMusicService()
    }

    private fun connectMusicService() {
        val musicServiceIntent = Intent(this@PlayListActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun prepareRecyclerView() {
        binding.rvPlayList.apply {
            layoutManager =
                LinearLayoutManager(this@PlayListActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setEvents() {
        binding.apply {
            buttonBack.setOnClickListener {
                finish()
            }
            fab.setOnClickListener {
                startActivity(Intent(this@PlayListActivity, SelectSongActivity::class.java))
            }
            buttonHome.setOnClickListener {
                startActivity(Intent(this@PlayListActivity, SongActivity::class.java))
            }
            fabUpload.setOnClickListener {
            }
        }
    }

    private fun dataBinding() {
    }

    private fun init() {
        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
    }
}