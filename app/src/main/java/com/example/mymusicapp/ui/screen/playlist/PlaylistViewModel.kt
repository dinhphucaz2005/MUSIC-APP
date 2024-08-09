package com.example.mymusicapp.ui.screen.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Playlist
import com.example.mymusicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.launch

@UnstableApi
class PlaylistViewModel(
    private val repository: PlaylistRepository = AppModule.providePlaylistRepository()
) : ViewModel() {

    val playlistState = mutableStateListOf<Playlist>()

    init {
        viewModelScope.launch {
            playlistState.addAll(repository.getPlaylist())
        }
    }



}