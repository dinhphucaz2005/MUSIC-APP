package com.example.musicapp.helper

import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toUri
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.getAuthor
import com.example.musicapp.extension.getDuration
import com.example.musicapp.extension.getId
import com.example.musicapp.extension.getImageBitmap
import com.example.musicapp.extension.getTitle
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File

object MediaRetrieverHelper {

    private const val TAG = "MediaRetrieverHelper"
    private const val NUMBER_OF_THREADS = 6

    suspend fun getSongsInfo(filePaths: List<String>): List<Song> {
        return withContext(Dispatchers.IO) {
            val jobs = mutableListOf<Deferred<List<Song>>>()

            val chunkSize = (filePaths.size + NUMBER_OF_THREADS - 1) / NUMBER_OF_THREADS

            for (i in 0 until NUMBER_OF_THREADS) {
                jobs.add(async {
                    val retriever = MediaMetadataRetriever()
                    val result = mutableListOf<Song>()
                    for (j in i * chunkSize until minOf((i + 1) * chunkSize, filePaths.size)) {
                        getSongInfo(retriever, filePaths[j])?.let { result.add(it) }
                    }
                    retriever.release()
                    result
                })
            }

            jobs.flatMap { it.await() }
        }
    }

    private fun getSongInfo(retriever: MediaMetadataRetriever, path: String): Song? {
        return try {
            retriever.setDataSource(path)
            val file = File(path)

            Song(
                id = file.getId(),
                uri = file.toUri(),
                title = retriever.getTitle() ?: file.nameWithoutExtension,
                author = retriever.getAuthor(),
                duration = retriever.getDuration(),
                thumbnail = retriever.getImageBitmap()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving song info for path: $path", e)
            null
        }
    }
}

