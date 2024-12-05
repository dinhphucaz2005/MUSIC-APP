package com.example.musicapp.youtube.presentation.home

import androidx.lifecycle.ViewModel
import com.example.innertube.CustomYoutube
import com.example.innertube.pages.HomePage
import com.example.musicapp.extension.load
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor() : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _home = MutableStateFlow(HomePage(emptyList()))
    val home: StateFlow<HomePage> = _home.asStateFlow()

    init {
        load(_isLoading) {
            _home.update { CustomYoutube.loadHome() }
        }
    }
}
