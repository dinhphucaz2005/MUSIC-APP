package com.example.musicapp.other.domain.repository

import com.example.musicapp.other.domain.model.FirebaseSong
import com.example.musicapp.other.domain.model.LocalSong

interface CloudRepository {

    suspend fun load(): List<FirebaseSong>

    fun upload(localSongs: List<LocalSong>)
}