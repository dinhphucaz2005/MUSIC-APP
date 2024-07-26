package com.example.mymusicapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import kotlinx.coroutines.launch


@UnstableApi
class MainViewModel(
    private val songFileRepository: SongFileRepository = AppModule.provideSongFileRepository(),
) : ViewModel() {

    val songListLoaded = MutableLiveData(false)
    var songList = mutableStateListOf<Song>()

    fun getSongs(): List<Song> {
        return songList.map {
            it
        }
    }

    init {
        viewModelScope.launch {
            songList.addAll(songFileRepository.getAllAudioFiles())
            songListLoaded.postValue(true)
        }
    }

}