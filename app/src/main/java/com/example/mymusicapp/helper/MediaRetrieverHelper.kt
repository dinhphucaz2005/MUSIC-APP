package com.example.mymusicapp.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.mymusicapp.domain.model.Song
import java.io.ByteArrayOutputStream
import java.io.File

object MediaRetrieverHelper {

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
                duration
            )
            songs.add(song)
        }
        return songs
    }

    fun getBitmap(context: Context, imageUri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        val scaledBitmap = scaleBitmap(bitmap, 0.3f)
        return scaledBitmap
    }

    private fun scaleBitmap(bitmap: Bitmap, scale: Float): Bitmap {
        val width = (bitmap.width * scale).toInt()
        val height = (bitmap.height * scale).toInt()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        val byteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}