package com.example.musicapp.youtube.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.innertube.CustomYoutube
import com.example.innertube.pages.ArtistPage
import com.example.innertube.pages.HomePage
import com.example.innertube.pages.PlaylistPage
import com.example.musicapp.extension.load
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nd.phuc.cache.domain.CacheRepository
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val cacheRepository: CacheRepository
) : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _home = MutableStateFlow(HomePage(emptyList()))
    val home: StateFlow<HomePage> = _home.asStateFlow()

    private val _playlist = MutableStateFlow<PlaylistPage?>(null)
    val playlist: StateFlow<PlaylistPage?> = _playlist.asStateFlow()

    private val _artistPage = MutableStateFlow<ArtistPage?>(null)
    val artistPage: StateFlow<ArtistPage?> = _artistPage.asStateFlow()


    init {
        reload()
    }

    fun reload() {
        load(_isLoading) {
            _home.update { CustomYoutube.loadHome() }
        }
    }

    fun loadPlaylist(playlistId: String) = load(_isLoading) {
        val playlist = cacheRepository.getPlaylist(playlistId)
        if (playlist != null) {
            _playlist.value = playlist
            return@load
        }
        CustomYoutube
            .playlist(playlistId)
            .onSuccess {
                _playlist.value = it
                cacheRepository.insertPlaylist(it)
            }
            .onFailure {
                Log.e("YoutubeViewModel", "Failed to load playlist", it)
            }
    }

    fun resetPlaylist() {
        _playlist.value = null
    }


    fun loadArtist(artistId: String) = load(_isLoading) {
        CustomYoutube
            .artist(artistId)
            .onSuccess {
                _artistPage.value = it
            }
            .onFailure {
                Log.e("YoutubeViewModel", "Failed to load artist", it)
            }

    }

}
