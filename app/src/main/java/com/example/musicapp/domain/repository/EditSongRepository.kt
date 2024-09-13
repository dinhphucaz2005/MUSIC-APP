package com.example.musicapp.domain.repository

import android.net.Uri
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Song

interface EditSongRepository {

    suspend fun saveSongFile(
        fileName: String,
        title: String?,
        artist: String?,
        imageUri: Uri?,
        song: Song,
        onSaveFile: ResultCallback<String> // string -> new file path
    )
}