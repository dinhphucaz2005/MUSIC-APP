package com.example.musicapp.domain.repository

import com.example.musicapp.domain.model.Song

interface UploadRepository {

    suspend fun upload(song: Song)
}