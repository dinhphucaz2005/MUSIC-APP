package com.example.musicapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.repository.CloudRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloudViewModel @Inject constructor(
    private val cloudRepository: CloudRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CloudViewModel"
    }

    private val _songs = MutableStateFlow<List<ServerSong>>(emptyList())
    val songs: StateFlow<List<ServerSong>> = _songs.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchSongs()
    }

    private fun fetchSongs() {
        viewModelScope.launch {
            Log.d(TAG, "fetchSongs: ")
            when (val result = cloudRepository.loadMore()) {
                is AppResource.Success -> {
                    result.data?.let {
                        _songs.update { it.toMutableList().apply { clear() } }
                    }
                }

                is AppResource.Error -> {
                    _errorMessage.update { result.error }
                }
            }
        }
    }
}
