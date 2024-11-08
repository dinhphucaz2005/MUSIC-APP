package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.repository.CloudRepository
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.util.EventData
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel
class CloudViewModel @Inject constructor(
    private val cloudRepository: CloudRepository,
    private val playListRepository: PlayListRepository,
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {


    private val _songs = MutableStateFlow<List<ServerSong>>(emptyList())
    val songs: StateFlow<List<ServerSong>> = _songs.asStateFlow()

    init {
        viewModelScope.launch {
            cloudRepository.loadMore().let { result ->
                when (result) {
                    is AppResource.Success -> {
                        _songs.value = result.data ?: emptyList()
                    }

                    is AppResource.Error -> {
                        EventBus.getDefault().postSticky(EventData(result.error))
                    }
                }
            }
        }
    }

    fun upload() = viewModelScope.launch {
        playListRepository.getLocalPlayList().value.songs.let {
            cloudRepository.upload(it)
        }
    }

    fun playSongAtIndex(index: Int) {
        mediaControllerManager.playServerSongs(index, _songs.value)
    }

}
