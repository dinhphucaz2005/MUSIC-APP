package com.example.mymusicapp.helper

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.extension.toScaledImageBitmap
import java.io.File

object MediaRetrieverHelper {

    fun getAllInfo(filePath: String, retriever: MediaMetadataRetriever): Song? {
        return try {
            retriever.setDataSource(filePath)
            val fileName = File(filePath).name
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationStr?.toLongOrNull()
            val embeddedPicture = retriever.embeddedPicture
            val imageBitmap: ImageBitmap? = embeddedPicture?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
            }
            Song(
                fileName,
                Uri.fromFile(File(filePath)),
                filePath,
                title,
                artist,
                imageBitmap,
                imageBitmap?.toScaledImageBitmap(0.3f),
                duration
            )
        } catch (e: Exception) {
            Log.d("MediaRetrieverHelper", "getAllInfo: ${e.message}")
            null
        }
    }

    fun getAllInfo(filePathLists: List<String>): List<Song> {
        val retriever = MediaMetadataRetriever()
        val songs = mutableListOf<Song>()
        filePathLists.forEach { filePath ->
            retriever.setDataSource(filePath)
            val fileName = File(filePath).name
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationStr?.toLongOrNull()
            val embeddedPicture = retriever.embeddedPicture
            val imageBitmap: ImageBitmap? = if (embeddedPicture != null) {
                BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size)
                    .asImageBitmap()
            } else {
                null
            }
            val song = Song(
                fileName,
                Uri.fromFile(File(filePath)),
                filePath,
                title,
                artist,
                imageBitmap,
                imageBitmap?.toScaledImageBitmap(0.3f),
                duration
            )
            songs.add(song)
        }
        retriever.release()
        return songs
    }
}