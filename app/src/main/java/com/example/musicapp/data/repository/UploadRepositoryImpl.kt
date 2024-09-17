package com.example.musicapp.data.repository

import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.UploadRepository
import com.example.musicapp.extension.getFileNameWithoutExtension
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

private data class SongDB(
    val title: String?,
    val author: String?,
    val url: String,
) {
    constructor(song: Song, url: String) : this(song.title, song.author, url)
}


class UploadRepositoryImpl(
    private val storage: StorageReference,
    private val database: DatabaseReference
) : UploadRepository {
    override suspend fun upload(song: Song, index: Int) {
        if (song.uri == null)
            return
        val fileRef = storage.child("/songs/${song.fileName.getFileNameWithoutExtension()}")
        val uploadTask = fileRef.putFile(song.uri)

        val test: (Exception) -> Unit = {
            println("${song.fileName.getFileNameWithoutExtension()}: ${it.message}")
        }
        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                database.child("songs/${index}")
                    .setValue(SongDB(song, downloadUrl.toString()))
                    .addOnFailureListener { test(it) }
            }.addOnFailureListener { test(it) }
        }.addOnFailureListener { test(it) }
    }
}