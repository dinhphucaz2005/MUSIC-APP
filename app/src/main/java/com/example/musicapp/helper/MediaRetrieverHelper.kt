package com.example.musicapp.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.toScaledBitmap
import java.io.File

object MediaRetrieverHelper {

    fun getAllInfo(filePathLists: List<String>, songIds: List<Long>): MutableList<Song> {
        val retriever = MediaMetadataRetriever()
        val songs = mutableListOf<Song>()
        filePathLists.forEachIndexed { index, filePath ->
            retriever.setDataSource(filePath)
            val fileName = File(filePath).name
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationStr?.toLongOrNull()
            val embeddedPicture = retriever.embeddedPicture
            val bitmap: Bitmap? = embeddedPicture?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size)
            }
            val song = Song(
                songIds[index],
                fileName,
                Uri.fromFile(File(filePath)),
                filePath,
                title,
                artist,
                bitmap?.toScaledBitmap(0.3f)?.asImageBitmap(),
                bitmap?.asImageBitmap(),
                duration
            )
            songs.add(song)
        }
        retriever.release()
        return songs
    }

    fun getSongInfo(retriever: MediaMetadataRetriever, filePath: String, index: Long): Song {
        retriever.setDataSource(filePath)
        val fileName = File(filePath).name
        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val durationStr =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val duration = durationStr?.toLongOrNull()
        val embeddedPicture = retriever.embeddedPicture
        val bitmap: Bitmap? = embeddedPicture?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
        return Song(
            index,
            fileName,
            Uri.fromFile(File(filePath)),
            filePath,
            title,
            artist,
            bitmap?.toScaledBitmap(0.3f)?.asImageBitmap(),
            bitmap?.asImageBitmap(),
            duration
        )
    }
}


