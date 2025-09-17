package nd.phuc.musicapp.helper

import android.media.MediaMetadataRetriever
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import nd.phuc.core.extension.getAuthor
import nd.phuc.core.extension.getDuration
import nd.phuc.core.extension.getImageBitmap
import nd.phuc.core.extension.getTitle
import nd.phuc.core.model.LocalSong
import nd.phuc.core.model.ThumbnailSource
import java.io.File

object MediaRetrieverHelper {

    private const val TAG = "MediaRetrieverHelper"
    private const val NUMBER_OF_THREADS = 6

    private val hashMap = hashMapOf<String, LocalSong>() // <Path, LocalSong>
    private var lastExtract = System.currentTimeMillis()

    suspend fun extracts(filePaths: List<String>): List<LocalSong> {
        if (filePaths.isEmpty()) return emptyList()
        return withContext(Dispatchers.IO) {
            val chunkSize = (filePaths.size + NUMBER_OF_THREADS - 1) / NUMBER_OF_THREADS

            // Dividing filePaths into smaller chunks
            val chunks = filePaths.chunked(chunkSize)

            // Create a list of jobs to process songs in parallel
            val jobs = chunks.map { chunk ->
                async {
                    val retriever = MediaMetadataRetriever()
                    val result = mutableListOf<LocalSong>()

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


    private fun extract(retriever: MediaMetadataRetriever, path: String): LocalSong? {
        return try {
            val file = File(path)
            if (!file.exists()) { // File not found
                hashMap.remove(path)
                return null
            }
            if (file.lastModified() < lastExtract && hashMap.containsKey(path)) { // File not modified
                return hashMap[path]
            }

            retriever.setDataSource(path)

            val localSong = LocalSong(
                title = retriever.getTitle() ?: file.nameWithoutExtension,
                artist = retriever.getAuthor(),
                thumbnailSource = ThumbnailSource.FromBitmap(retriever.getImageBitmap()),
                durationMillis = retriever.getDuration(),
                filePath = path,
            )

            hashMap[path] = localSong // Save song to hashmap to avoid extract again
            localSong
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving song info for path: $path", e)
            null
        }
    }

    fun get(path: String): LocalSong? {
        return hashMap[path]
    }
}


