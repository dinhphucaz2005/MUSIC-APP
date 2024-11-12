package com.example.musicapp.domain.repository

import com.example.musicapp.domain.model.Song

interface CloudRepository {

    suspend fun load(): List<Song>

    fun upload(songs: List<Song>)
}