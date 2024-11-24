package com.example.musicapp.data

import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toFile
import com.example.musicapp.domain.model.AudioSource
import com.example.musicapp.domain.model.Identifiable
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.model.ThumbnailSource
import com.example.musicapp.extension.DEFAULT_COMPRESS_FORMAT
import com.example.musicapp.extension.toByteArray
import com.example.musicapp.helper.FileHelper
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
        override val id: String,
        val title: String,
        val artist: String,
        val songUri: String,
        val thumbnailUri: String?,
        val durationMillis: Long,
    ) : Identifiable {
        constructor() : this(
            id = UUID.randomUUID().toString(),
            title = "Unknown name",
            artist = "Unknown artist",
            songUri = "",
            thumbnailUri = null,
            durationMillis = 0L
        )

        constructor(id: String?, song: Song, songUri: String, imageUri: String?) : this(
            id = id ?: UUID.randomUUID().toString(),
            title = song.title,
            artist = song.artist,
            songUri = songUri,
            thumbnailUri = imageUri,
            durationMillis = song.durationMillis
        )
    }


    private val databaseChild = database.child(FirebaseKey.SONGS)
    private val storageChild = storage.child(FirebaseKey.SONGS)

    fun uploadSongs(songs: List<Song>) {
        CoroutineScope(Dispatchers.IO).launch {
            val retriever = MediaMetadataRetriever()
            val tasks = songs.mapNotNull { song ->
                try {
                    Log.d(TAG, "uploadSongs: ${song.title}")
                    val file = when (val audioSource = song.audioSource) {
                        is AudioSource.FromLocalFile -> audioSource.uri.toFile()
                        is AudioSource.FromUrl -> null
                    }
                    if (file != null)
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

    private suspend fun uploadSongToStorage(song: Song, file: File?, id: String) {
        val thumbnailUri = when (val thumbnailSource = song.thumbnailSource) {
            is ThumbnailSource.FromUrl ->
                thumbnailSource.url

            is ThumbnailSource.FromBitmap -> storageChild.child(FirebaseKey.SONG_THUMBNAILS)
                .child("$id.$DEFAULT_COMPRESS_FORMAT")
                .putStream(ByteArrayInputStream(thumbnailSource.imageBitmap.toByteArray()))
                .await().storage.downloadUrl.await().toString()
        }

        val songUri = when (val audioSource = song.audioSource) {
            is AudioSource.FromLocalFile -> storageChild.child("$id | ${file?.name}")
                .putFile(audioSource.uri)
                .await().storage.downloadUrl.await().toString()

            is AudioSource.FromUrl -> audioSource.url
        }

        val serverSong = ServerSong(id, song, songUri, thumbnailUri)
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