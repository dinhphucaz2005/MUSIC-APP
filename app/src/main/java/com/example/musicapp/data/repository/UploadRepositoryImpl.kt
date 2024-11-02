package com.example.musicapp.data.repository

import android.util.Log
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.UploadRepository
import com.example.musicapp.extension.getFileNameWithoutExtension
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.util.UUID

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

    companion object {
        private const val TAG = "UploadRepositoryImpl"
    }

    override suspend fun upload(song: Song) {
        song.uri ?: return

        val fileRef = storage.child(song.id.toString())
//        val fileRef = storage.child("/songs/${song.fileName.getFileNameWithoutExtension()}")

        val uploadTask = fileRef.putFile(song.uri)

        val handleError: (Exception) -> Unit = {
//            Log.d(TAG, "upload: ${song.fileName.getFileNameWithoutExtension()}: ${it.message}")
        }

        // Thêm listener cho upload thành công
        uploadTask.addOnSuccessListener {
            // Lấy URL tải về sau khi upload thành công
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                // Tạo một ServerSong và lưu vào Realtime Database
                val serverSong = ServerSong(song, downloadUrl.toString(), null)
                database.child("songs/${serverSong.id}")
                    .setValue(serverSong)
                    .addOnSuccessListener {
                        Log.d(TAG, "Bài hát đã được lưu vào database với ID: ${song.id}")
                    }
                    .addOnFailureListener { handleError(it) }
            }.addOnFailureListener { handleError(it) }
        }.addOnFailureListener { handleError(it) }
    }

}