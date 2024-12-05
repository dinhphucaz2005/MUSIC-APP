package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.repository.CloudRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val cloudRepository: CloudRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun uploadSongs(songs: List<Song>) = cloudRepository.upload(songs)
}
