package com.example.musicapp.viewmodels

import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import com.example.innertube.CustomYoutube
import com.example.innertube.pages.HomePage
import com.example.musicapp.extension.load
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {
    companion object {
        const val TAG = "YoutubeViewModel"
    }

    private val _home = MutableStateFlow(HomePage(emptyList()))
    val home: StateFlow<HomePage> = _home.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        load(_isLoading) {
            _home.update { CustomYoutube.loadHome() }
            _home.value
        }
    }
}
