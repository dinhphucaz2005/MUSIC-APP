package com.example.musicapp.ui.screen.cloud

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.CloudRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloudViewModel @Inject constructor(
    private val cloudRepository: CloudRepository
) : ViewModel() {

    private var currentPage = 0
    private val pageSize = 10

    private val _songs = mutableStateListOf<Song>()
    val songs: List<Song> = _songs

    init {
        viewModelScope.launch {
            cloudRepository.loadSongs("", currentPage++, pageSize).let { result ->
                when (result) {
                    is AppResource.Success -> {
                        result.data?.forEachIndexed { _, song ->
                            _songs.add(song)
                        }
                    }

                    is AppResource.Error -> {
                        println(result.error)
                    }
                }
            }
        }
    }

}