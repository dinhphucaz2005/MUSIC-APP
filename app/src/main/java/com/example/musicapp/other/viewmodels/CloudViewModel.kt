package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.constants.DEFAULT_DURATION_MILLIS
import com.example.musicapp.extension.load
import com.example.musicapp.other.domain.model.FirebaseSong
import com.example.musicapp.other.domain.repository.CloudRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CloudViewModel @Inject constructor(
    private val cloudRepository: CloudRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading
        .onStart { if (_songs.value.isEmpty()) loadSong() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(DEFAULT_DURATION_MILLIS), false)

    private val _songs = MutableStateFlow<List<FirebaseSong>>(emptyList())
    val songs: StateFlow<List<FirebaseSong>> = _songs.asStateFlow()

    private fun loadSong() = load(_isLoading) {
        _songs.update { cloudRepository.load() }
    }


}
