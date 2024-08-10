package com.example.mymusicapp.domain.repository

import android.net.Uri
import com.example.mymusicapp.callback.ResultCallback

interface EditSongRepository {
    suspend fun saveSongFile(
        songFilePath: String,
        imageUri: Uri,
        title: String,
        artist: String,
        newFileName: String,
        onSaveFile: ResultCallback<String> // string -> new file path
    )
}