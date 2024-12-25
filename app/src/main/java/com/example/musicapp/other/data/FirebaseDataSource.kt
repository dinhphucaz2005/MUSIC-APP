package com.example.musicapp.other.data

import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toFile
import com.example.musicapp.extension.DEFAULT_COMPRESS_FORMAT
import com.example.musicapp.extension.toByteArray
import com.example.musicapp.helper.FileHelper
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.util.FirebaseKey
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import java.io.ByteArrayInputStream
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    storage: StorageReference, database: DatabaseReference,
) {

    companion object {
        private const val TAG = "FirebaseDataSource"
    }

    @Serializable
    data class ServerSong(
        val id: String = UUID.randomUUID().toString(),
        val title: String = "Unknown name",
        val artist: String = "Unknown artist",
        val songUri: String = "",
        val thumbnailUri: String? = null,
        val durationMillis: Long = 0L,
    ) {
        constructor(id: String?, localSong: LocalSong, songUri: String, imageUri: String?) : this(
            id = id ?: UUID.randomUUID().toString(),
            title = localSong.title,
            artist = localSong.artist,
            songUri = songUri,
            thumbnailUri = imageUri,
            durationMillis = localSong.durationMillis ?: 0L
        )
    }


    private val databaseChild = database.child(FirebaseKey.SONGS)
    private val storageChild = storage.child(FirebaseKey.SONGS)

    fun uploadSongs(localSongs: List<LocalSong>) {
        CoroutineScope(Dispatchers.IO).launch {
            val retriever = MediaMetadataRetriever()
            val tasks = localSongs.mapNotNull { song ->
                try {
                    Log.d(TAG, "uploadSongs: ${song.title}")
                    val file = song.audio.toFile()
                    retriever.setDataSource(file.absolutePath)
                    val id = FileHelper.getFileHash(file) ?: song.id

                    async {
                        databaseChild.child(id).get().await().let { snapshot ->
                            if (snapshot.exists()) {
                                Log.d(TAG, "Song already exists: ${song.title}")
                                return@async null
                            }
                            try {
                                uploadSongToStorage(song, file, id)
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "Failed to upload song: ${song.title} due to ${e.message}"
                                )
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
    }

    private suspend fun uploadSongToStorage(localSong: LocalSong, file: File?, id: String) {
        val thumbnailUri = when (val thumbnailSource = localSong.thumbnailSource) {
            is ThumbnailSource.FromUrl ->
                thumbnailSource.url

            is ThumbnailSource.FromBitmap -> storageChild.child(FirebaseKey.SONG_THUMBNAILS)
                .child("$id.$DEFAULT_COMPRESS_FORMAT")
                .putStream(ByteArrayInputStream(thumbnailSource.imageBitmap.toByteArray()))
                .await().storage.downloadUrl.await().toString()
        }

        val songUri = storageChild.child("$id | ${file?.name}")
            .putFile(localSong.audio)
            .await().storage.downloadUrl.await().toString()

        val serverSong = ServerSong(id, localSong, songUri, thumbnailUri)
        databaseChild.child(id).setValue(serverSong)
    }

    suspend fun load(): List<ServerSong> {
        val songList = mutableListOf<ServerSong>()
        val page = 0
        val size = 20
        val startAfter = page * size
        val snapshot = databaseChild
            .orderByKey().startAt(startAfter.toString())
            .limitToFirst(size).get().await()

        for (child in snapshot.children) {
            val serverSong = child.getValue(ServerSong::class.java)
            serverSong?.let { songList.add(it) }
        }
        return songList
    }


}