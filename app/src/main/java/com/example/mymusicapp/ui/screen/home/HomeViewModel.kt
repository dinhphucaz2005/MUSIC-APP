package com.example.mymusicapp.ui.screen.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import kotlinx.coroutines.launch

@UnstableApi
class HomeViewModel(
    private val repository: SongFileRepository = AppModule.provideSongFileRepository()
) : ViewModel() {

    val songList = mutableStateListOf<Song>()

    init {
        viewModelScope.launch {
            repository.getAllAudioFiles().collect {
                songList.clear()
                songList.addAll(it)
            }
        }
    }

    fun search(searchQuery: String) {
        viewModelScope.launch {
            songList.clear()
            repository.search(searchQuery)?.let { songList.addAll(it) }
        }
    }

    fun reload() {
        viewModelScope.launch {
            repository.reloadFiles()
        }
    }


}