package nd.phuc.musicapp.helper

import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import nd.phuc.musicapp.model.LocalSong
import nd.phuc.musicapp.model.ThumbnailSource
import java.io.File

fun MediaMetadataRetriever.getDuration(): Long =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L

fun MediaMetadataRetriever.getTitle(): String? =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

fun MediaMetadataRetriever.getAuthor(): String =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown"

fun MediaMetadataRetriever.getThumbnailByteArray(): ByteArray? = this.embeddedPicture

sealed class ExtractLocalSongResult {
    data object Idle : ExtractLocalSongResult()
    data object InProgress : ExtractLocalSongResult()
    data object Finished : ExtractLocalSongResult()
    data class Success(val song: LocalSong) : ExtractLocalSongResult()
    data class NotFound(val path: String) : ExtractLocalSongResult()
    data class Error(val path: String, val exception: Exception) : ExtractLocalSongResult()
}

object LocalSongExtractor {
    private const val TAG = "LocalDataSource"
    private const val NUMBER_OF_THREADS = 6
    private lateinit var thumbnailDir: File
    fun initialize(context: Context) {
        thumbnailDir = File(context.cacheDir, "thumbnails")
        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdirs()
        }
    }

    private val hashMap = hashMapOf<String, LocalSong>() // <Path, LocalSong>

    fun getCachedSong(filePath: String): LocalSong? {
        return hashMap[filePath]
    }

    fun extracts(
        context: Context,
        filePaths: List<String>,
        onSongExtracted: (ExtractLocalSongResult) -> Unit,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val chunkSize = (filePaths.size + NUMBER_OF_THREADS - 1) / NUMBER_OF_THREADS

            // Dividing filePaths into smaller chunks
            val files = filePaths.ifEmpty { getAllLocalFilePaths(context) }
            if (files.isEmpty()) {
                Log.w(TAG, "extracts: no local audio files found")
                onSongExtracted(ExtractLocalSongResult.Finished)
                return@launch
            }

            val chunks = files.chunked(if (chunkSize == 0) 1 else chunkSize)

            // Create a list of jobs to process songs in parallel
            val jobs = chunks.map { chunk ->
                async {
                    val retriever = MediaMetadataRetriever()
                    // Process each file in the chunk
                    chunk.forEach { path ->
                        val file = File(path)
                        if (!file.exists()) {
                            Log.w(TAG, "extracts: file not found: $path")
                            hashMap.remove(path)
                            onSongExtracted(ExtractLocalSongResult.NotFound(path))
                            return@forEach
                        }
                        val localSong = hashMap[path]
                        if (localSong != null) {
                            onSongExtracted(ExtractLocalSongResult.Success(localSong))
                            return@forEach
                        }
                        extract(retriever, file).let {
                            hashMap[path] = it.let { result ->
                                when (result) {
                                    is ExtractLocalSongResult.Success -> result.song
                                    else -> return@let null
                                }
                            }!!
                            onSongExtracted(it)
                        }
                    }

                    retriever.release() // Release the MediaMetadataRetriever instance in each chunk
                }
            }

            jobs.awaitAll()
            onSongExtracted(ExtractLocalSongResult.Finished)
        }
    }


    private fun extract(retriever: MediaMetadataRetriever, file: File): ExtractLocalSongResult {
        return try {
            retriever.setDataSource(file.path)

            val title = retriever.getTitle() ?: file.nameWithoutExtension
            val artist = retriever.getAuthor()
            val duration = retriever.getDuration()

            val tempSong = LocalSong(
                title = title,
                artist = artist,
                thumbnailSource = ThumbnailSource.None,
                durationMillis = duration,
                filePath = file.path,
                isLiked = false
            )

            val cachedThumbnailPath = getThumbnailCachePath(tempSong)
            if (cachedThumbnailPath != null) {
                return ExtractLocalSongResult.Success(
                    tempSong.copy(
                        thumbnailSource = ThumbnailSource.FilePath(cachedThumbnailPath)
                    )
                )
            }


            val thumbnailPath = cacheThumbnail(
                tempSong.copy(
                    thumbnailSource = ThumbnailSource.FromByteArray(
                        retriever.getThumbnailByteArray()
                    )
                ), thumbnailDir
            )
            val thumbnailSource = if (thumbnailPath != null) {
                ThumbnailSource.FilePath(thumbnailPath)
            } else {
                ThumbnailSource.None
            }

            val localSong = tempSong.copy(thumbnailSource = thumbnailSource)

            ExtractLocalSongResult.Success(localSong)
        } catch (e: Exception) {
            Log.e(TAG, "extract: failed to extract metadata for file: ${file.path}", e)
            ExtractLocalSongResult.Error(file.path, e)
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
            Log.e(TAG, "getAllLocalFilePaths: query failed", e)
            null
        }

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                val filePath = it.getString(dataIndex)
                filePaths.add(filePath)
            }
        } ?: Log.w(TAG, "getAllLocalFilePaths: cursor is null - no results or query failed")

        Log.d(TAG, "getAllLocalFilePaths: found ${filePaths.size} audio file paths")
        return filePaths
    }

    private fun nameForCachedFile(song: LocalSong): String {
        return "${song.filePath.hashCode().toString().replace("-", "")}.jpg"
    }


    private fun getThumbnailCachePath(song: LocalSong): String? {
        val file = File(thumbnailDir, nameForCachedFile(song))
        return if (file.exists()) {
            file.absolutePath
        } else {
            null
        }
    }

    private fun cacheThumbnail(song: LocalSong, cacheDir: File): String? {
        return when (val thumb = song.thumbnailSource) {
            is ThumbnailSource.FromByteArray -> {
                thumb.byteArray?.let { bytes ->
                    val file = File(cacheDir, nameForCachedFile(song))
                    if (!file.exists()) {
                        try {
                            file.writeBytes(bytes)
                            Log.d(TAG, "Cached thumbnail: ${file.absolutePath}")
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to cache thumbnail", e)
                            return null
                        }
                    }
                    file.absolutePath
                }
            }

            is ThumbnailSource.FromUrl -> thumb.url
            is ThumbnailSource.FilePath -> thumb.path
            else -> null
        }
    }

}