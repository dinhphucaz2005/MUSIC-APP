package com.example.mymusicapp.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.mymusicapp.callback.ResultCallback
import com.example.mymusicapp.domain.repository.EditSongRepository
import com.example.mymusicapp.helper.MediaStoreHelper
import com.mpatric.mp3agic.Mp3File
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.math.min

class EditSongRepositoryImpl @Inject constructor(
    private val context: Context
) : EditSongRepository {

    companion object {
        const val TAG = "EditSongRepositoryImpl"
    }

    override suspend fun saveSongFile(
        songFilePath: String,
        imageUri: Uri,
        title: String,
        artist: String,
        newFileName: String,
        onSaveFile: ResultCallback<String>
    ) {

        try {
            val mp3File = Mp3File(songFilePath)
            val id3v2Tag = mp3File.id3v2Tag
            id3v2Tag.title = title
            id3v2Tag.artist = artist
            val contentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            inputStream?.use { input ->
                val originalBitmap = BitmapFactory.decodeStream(input)
                val width = originalBitmap.width
                val height = originalBitmap.height
                Log.d(TAG, "saveSongFile: $width*$height")

                var newWidth = 400
                var newHeight = 400

                if (newWidth > width || newHeight > height) {
                    throw Exception("Image dimensions are too small")
                }

                val scaleFactor = min(width, height).toFloat() / newWidth.toFloat()
                newWidth = (width / scaleFactor).toInt()
                newHeight = (height / scaleFactor).toInt()

                Log.d(TAG, "saveSongFile: $newWidth*$newHeight")

                val scaleBitmap =
                    Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false)

                val left = (scaleBitmap.width - newWidth) / 2
                val top = (scaleBitmap.height - newHeight) / 2
                val right = left + newWidth
                val bottom = top + newHeight

                val croppedBitmap =
                    Bitmap.createBitmap(scaleBitmap, left, top, right, bottom)

                val outputStream = ByteArrayOutputStream()
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageData = outputStream.toByteArray()
                id3v2Tag.setAlbumImage(imageData, "image/jpeg")
            }
            val path = "/storage/emulated/0/Music/${newFileName}"
            mp3File.save(path)

            MediaStoreHelper.scanFile(
                path,
                context,
            ) { scannedPath, _ ->
                onSaveFile.onSuccess(scannedPath)
            }
        } catch (e: Exception) {
            onSaveFile.onFailure(e)
        }
    }
}