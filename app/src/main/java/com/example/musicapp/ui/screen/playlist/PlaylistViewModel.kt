package com.example.musicapp.ui.screen.playlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: PlaylistRepository
) : ViewModel() {
    fun offDialog() {
        showDialog.value = false
    }

    fun savePlaylist() {
        offDialog()
        viewModelScope.launch {
            repository.addPlaylist(Playlist(), object : ResultCallback<String> {
                override fun onSuccess(result: String) {

                }

                override fun onFailure(exception: Exception) {
                }
            })
        }
    }

    val playlistState = repository.getPlaylist()
    val showDialog = mutableStateOf(true)


}