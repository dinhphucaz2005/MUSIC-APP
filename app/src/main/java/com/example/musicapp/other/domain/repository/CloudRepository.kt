package com.example.musicapp.other.domain.repository

import com.example.musicapp.other.domain.model.Song

interface CloudRepository {

    suspend fun load(): List<Song>

    fun upload(songs: List<Song>)
}