package com.example.musicapp.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.getFileNameExtension
import com.example.musicapp.extension.toScaledBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Collections
import kotlin.system.measureTimeMillis

object MediaRetrieverHelper {

    private const val TAG = "MediaRetrieverHelper"
    private const val NUMBER_OF_THREADS = 6

    suspend fun getSongsInfo(
        filePaths: List<String>
    ): List<Song> {
        return withContext(Dispatchers.IO) {
            val result = Collections.synchronizedList(mutableListOf<Song>())
            val timeTaken = measureTimeMillis {

                val chunkSize = (filePaths.size + NUMBER_OF_THREADS - 1) / NUMBER_OF_THREADS

                val threads = mutableListOf<Thread>()


                for (i in 0..<NUMBER_OF_THREADS)
                    threads.add(Thread {
                        val retriever = MediaMetadataRetriever()
                        for (j in i * chunkSize..<minOf((i + 1) * chunkSize, filePaths.size)) {
                            getSongInfo(retriever, filePaths[j])?.let { result.add(it) }
                        }
                        retriever.release()
                    })

                for (i in 0..<NUMBER_OF_THREADS)
                    threads[i].start()
                for (i in 0..<NUMBER_OF_THREADS)
                    threads[i].join()
            }
            Log.d(TAG, "getSongsInfo: Time execution with $NUMBER_OF_THREADS $timeTaken ms")
            result
        }
    }

    private fun getSongInfo(retriever: MediaMetadataRetriever, filePath: String): Song? {
        return try {
            retriever.setDataSource(filePath)

            val fileName = File(filePath).name
            val title =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: fileName.getFileNameExtension()
            val author =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown"
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationStr?.toLongOrNull() ?: 0L

            val embeddedPicture = retriever.embeddedPicture
            val bitmap: Bitmap? = embeddedPicture?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size)
            }

            Song(
                id = null,
                fileName = fileName,
                uri = Uri.fromFile(File(filePath)),
                path = filePath,
                title = title,
                author = author,
                smallBitmap = bitmap?.toScaledBitmap(0.3f)?.asImageBitmap(),
                thumbnail = bitmap?.asImageBitmap(),
                duration = duration
            )
        } catch (e: Exception) {
            null
        }
    }
}


