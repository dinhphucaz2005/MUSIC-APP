package com.example.musicapp.helper

import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toUri
import com.example.musicapp.domain.model.AudioSource
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.model.ThumbnailSource
import com.example.musicapp.extension.getAuthor
import com.example.musicapp.extension.getDuration
import com.example.musicapp.extension.getFileId
import com.example.musicapp.extension.getImageBitmap
import com.example.musicapp.extension.getTitle
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File

object MediaRetrieverHelper {

    private const val TAG = "MediaRetrieverHelper"
    private const val NUMBER_OF_THREADS = 6

    private val hashMap = hashMapOf<String, Song>() // <Path, Song>
    private var lastExtract = System.currentTimeMillis()

    suspend fun extracts(filePaths: List<String>): List<Song> {
        if (filePaths.isEmpty()) return emptyList()
        return withContext(Dispatchers.IO) {
            val chunkSize = (filePaths.size + NUMBER_OF_THREADS - 1) / NUMBER_OF_THREADS

            // Dividing filePaths into smaller chunks
            val chunks = filePaths.chunked(chunkSize)

            // Create a list of jobs to process songs in parallel
            val jobs = chunks.map { chunk ->
                async {
                    val retriever = MediaMetadataRetriever()
                    val result = mutableListOf<Song>()

                    // Process each file in the chunk
                    chunk.forEach { path ->
                        extract(retriever, path)?.let { result.add(it) }
                    }

                    retriever.release() // Release the MediaMetadataRetriever instance in each chunk
                    result
                }
            }

            val songs = jobs.awaitAll().flatten()

            lastExtract = System.currentTimeMillis() // Update lastExtract time

            songs
        }
    }

    private fun extract(retriever: MediaMetadataRetriever, path: String): Song? {
        return try {
            retriever.setDataSource(path)
            val file = File(path)
            if (!file.exists()) { // File not found
                hashMap.remove(path)
                return null
            }
            if (file.lastModified() < lastExtract && hashMap.containsKey(path)) { // File not modified
                return hashMap[path]
            }

            val song = Song(
                id = file.getFileId(),
                title = retriever.getTitle() ?: file.nameWithoutExtension,
                artist = retriever.getAuthor(),
                audioSource = AudioSource.FromLocalFile(file.toUri()),
                thumbnailSource = ThumbnailSource.FromBitmap(retriever.getImageBitmap()),
                durationMillis = retriever.getDuration(),
            )

            hashMap[path] = song // Save song to hashmap to avoid extract again
            song
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving song info for path: $path", e)
            null
        }
    }
}


