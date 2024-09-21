package com.example.musicapp.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.extension.getFileNameExtension
import com.example.musicapp.helper.MediaStoreHelper
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
        fileName: String,
        title: String?,
        artist: String?,
        imageUri: Uri?,
        song: Song,
        onSaveFile: ResultCallback<String>
    ) {
        Log.d(TAG, "saveSongFile: $fileName")
        try {
            val mp3File = Mp3File(song.path)
            val id3v2Tag = mp3File.id3v2Tag
            title?.let { id3v2Tag.title = it }
            artist?.let { id3v2Tag.artist = it }
            val contentResolver = context.contentResolver
            imageUri?.let {
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
                inputStream?.use { input ->
                    val originalBitmap = BitmapFactory.decodeStream(input)
                    val width = originalBitmap.width
                    val height = originalBitmap.height

                    var newWidth = 400
                    var newHeight = 400

                    if (newWidth > width || newHeight > height) {
                        throw Exception("Image dimensions are too small")
                    }

                    val scaleFactor = min(width, height).toFloat() / newWidth.toFloat()
                    newWidth = (width / scaleFactor).toInt()
                    newHeight = (height / scaleFactor).toInt()

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
            }
            val fileExtension = song.path?.getFileNameExtension() ?: "mp3"
            val path = "/storage/emulated/0/Music/$fileName.$fileExtension"
            mp3File.save(path)
            MediaStoreHelper.scanFile(
                path,
                context,
            ) { _, _ ->
                onSaveFile.onSuccess("Saved successfully")
            }
        } catch (e: Exception) {
            onSaveFile.onFailure(e)
        }
    }
}