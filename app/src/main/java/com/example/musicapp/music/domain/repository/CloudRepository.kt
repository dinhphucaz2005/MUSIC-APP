package com.example.musicapp.music.domain.repository

import com.example.musicapp.music.domain.model.FirebaseSong
import com.example.musicapp.music.domain.model.LocalSong

interface CloudRepository {

    suspend fun load(): List<FirebaseSong>

    fun upload(localSongs: List<LocalSong>)
}