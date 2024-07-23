package com.example.mymusicapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.mymusicapp.data.repository.SongFileRepositoryImpl
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import kotlinx.coroutines.launch


class MainViewModel(
    private val songFileRepository: SongFileRepositoryImpl = AppModule.provideSongFileRepository(),
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