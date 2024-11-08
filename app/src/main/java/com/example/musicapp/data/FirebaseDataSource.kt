package com.example.musicapp.data

import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toFile
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.DEFAULT_COMPRESS_FORMAT
import com.example.musicapp.extension.toByteArray
import com.example.musicapp.helper.FileHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    storage: StorageReference, database: DatabaseReference
) {

    companion object {
        private const val TAG = "FirebaseDataSource"
    }

    private val songsRef = database.child("songs")
    private val storageRef = storage.child("songs")

    suspend fun uploadSongs(songs: List<Song>) = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        val tasks = songs.mapNotNull { song ->
            try {
                Log.d(TAG, "uploadSongs: ${song.title}")
                val file = song.uri.toFile()
                retriever.setDataSource(file.absolutePath)
                val id = FileHelper.getFileHash(file)

                async {
                    songsRef.child(id).get().await().let { snapshot ->
                        if (snapshot.exists()) {
                            Log.d(TAG, "Song already exists: ${song.title}")
                            return@async null
                        }
                        try {
                            uploadSongToStorage(song, file, id)
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to upload song: ${song.title} due to ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing song: ${song.title} with exception ${e.message}")
                null
            }
        }

        tasks.awaitAll()
        retriever.release()
    }

    private suspend fun uploadSongToStorage(song: Song, file: File, id: String) {
        val imageUri = storageRef.child("song_thumbnails").child("$id.$DEFAULT_COMPRESS_FORMAT")
            .putStream(ByteArrayInputStream(song.thumbnail.toByteArray()))
            .await().storage.downloadUrl.await().toString()

        val songUri = storageRef.child("$id | ${file.name}").putFile(song.uri)
            .await().storage.downloadUrl.await().toString()

        val serverSong = ServerSong(id, song, songUri, imageUri)
        songsRef.child(id).setValue(serverSong)
    }


    suspend fun loadMore(): List<ServerSong> {
        val songList = mutableListOf<ServerSong>()
        val page = 0
        val size = 20
        val startAfter = page * size
        val snapshot = songsRef
            .orderByKey().startAt(startAfter.toString())
            .limitToFirst(size).get().await()

        for (child in snapshot.children) {
            val serverSong = child.getValue(ServerSong::class.java)
            serverSong?.let { songList.add(it) }
        }
        return songList
    }


}