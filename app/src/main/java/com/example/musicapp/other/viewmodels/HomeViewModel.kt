package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import com.example.musicapp.extension.load
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.repository.CloudRepository
import com.example.musicapp.other.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val cloudRepository: CloudRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _songs = MutableStateFlow<List<LocalSong>>(emptyList())
    val songs = _songs.asStateFlow()


    fun loadSongs() = load(_isLoading) {
        _songs.update { songRepository.getLocalSong() }
    }

    fun uploadSongs() = cloudRepository.upload(_songs.value)


}