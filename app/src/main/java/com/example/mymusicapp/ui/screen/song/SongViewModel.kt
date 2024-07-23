package com.example.mymusicapp.ui.screen.song

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.data.dto.SongFileDTO

class SongViewModel : ViewModel() {
    fun printPosition(it: Float) {
        println(it)
    }

    val songFileDTO = mutableStateOf(SongFileDTO())

}