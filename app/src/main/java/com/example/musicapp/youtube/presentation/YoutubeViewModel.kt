package com.example.musicapp.youtube.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.innertube.CustomYoutube
import com.example.innertube.pages.HomePage
import com.example.innertube.pages.PlaylistPage
import com.example.musicapp.extension.load
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class YoutubeViewModel @Inject constructor() : ViewModel() {

    private val _playlist = MutableStateFlow<PlaylistPage?>(null)
    val playlist: StateFlow<PlaylistPage?> = _playlist.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _home = MutableStateFlow(HomePage(emptyList()))
    val home: StateFlow<HomePage> = _home.asStateFlow()

    init {
        load(_isLoading) {
            _home.update { CustomYoutube.loadHome() }
        }
    }

    fun loadPlaylist(playlistId: String) = load {
        CustomYoutube.playlist(playlistId).onSuccess {
            Log.d("YoutubeViewModel", "loadPlaylist: $it")
            _playlist.value = it
        }.onFailure {
            Log.e("YoutubeViewModel", "Failed to load playlist", it)
        }
    }

    fun resetPlaylist() {
        _playlist.value = null
    }

}
