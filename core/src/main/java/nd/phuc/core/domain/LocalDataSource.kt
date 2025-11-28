package nd.phuc.core.domain

import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.ThumbnailSource
import nd.phuc.core.extension.getAuthor
import nd.phuc.core.extension.getDuration
import nd.phuc.core.extension.getImageBitmap
import nd.phuc.core.extension.getTitle
import timber.log.Timber
import java.io.File

class LocalDataSource(
    private val context: Context,
) {
    companion object {
        private const val NUMBER_OF_THREADS = 6
    }

    private val hashMap = hashMapOf<String, LocalSong>() // <Path, LocalSong>
    private var lastExtract = System.currentTimeMillis()

    suspend fun extracts(filePaths: List<String>): List<LocalSong> {
        if (filePaths.isEmpty()) {
            Timber.d("extracts: received empty filePaths list")
            return emptyList()
        }
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

            Timber.d("extracts: extracted ${songs.size} songs from ${filePaths.size} paths")

            songs
        }
    }


    private fun extract(retriever: MediaMetadataRetriever, path: String): LocalSong? {
        return try {
            val file = File(path)
            if (!file.exists()) { // File not found
                Timber.w("extract: file not found: $path")
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
                isLiked = false, // Default value, should be updated from repository
            )

            hashMap[path] = localSong // Save song to hashmap to avoid extract again
            localSong
        } catch (e: Exception) {
            Timber.e(e, "Error retrieving song info for path: $path")
            null
        }
    }

    private fun getAllLocalFilePaths(context: Context): List<String> {
        val filePaths = mutableListOf<String>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val cursor = try {
            context.contentResolver.query(uri, projection, selection, null, sortOrder)
        } catch (e: Exception) {
            Timber.e(e, "getAllLocalFilePaths: query failed")
            null
        }

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                val filePath = it.getString(dataIndex)
                filePaths.add(filePath)
            }
        } ?: Timber.w("getAllLocalFilePaths: cursor is null - no results or query failed")

        Timber.d("getAllLocalFilePaths: found ${filePaths.size} audio file paths")
        if (filePaths.isNotEmpty()) {
            filePaths.take(5).forEach { Timber.d("sample path: $it") }
        }

        return filePaths
    }


    suspend fun get(): List<LocalSong> {
        val paths = getAllLocalFilePaths(context)
        return extracts(paths)
    }

    fun getSongByPath(path: String): LocalSong? = hashMap[path]
}