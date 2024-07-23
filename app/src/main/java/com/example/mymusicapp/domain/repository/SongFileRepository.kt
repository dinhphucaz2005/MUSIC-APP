package com.example.mymusicapp.domain.repository

import com.example.mymusicapp.domain.model.Song

interface SongFileRepository {

    suspend fun getAllAudioFiles(): ArrayList<Song>

}