package com.example.mymusicapp.ui.screen.edit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.helper.MediaStoreHelper
import com.example.mymusicapp.util.EventData
import com.example.mymusicapp.util.MediaControllerManager
import com.mpatric.mp3agic.Mp3File
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

@UnstableApi
class EditViewModel : ViewModel() {

    companion object {
        const val TAG = "EditViewModel"
    }

    @SuppressLint("Recycle")
    fun saveSongFile(uri: Uri?) {
        viewModelScope.launch {

            val song = MediaControllerManager.getSong()

            try {
                val mp3File = Mp3File(song.path)
                val id3v2Tag = mp3File.id3v2Tag
                id3v2Tag.title = title.value
                id3v2Tag.artist = artist.value

                if (uri != null) {
                    val context = AppModule.provideAppContext()
                    val contentResolver = context.contentResolver
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
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
                }
                val path = "/storage/emulated/0/Music/${fileName.value}"
                mp3File.save(path)
                MediaStoreHelper.scanFile(path)
                EventBus.getDefault().postSticky(EventData("Song saved successfully"))
            } catch (e: Exception) {
                Log.d(TAG, "saveSongFile: ${e.message}")
                EventBus.getDefault()
                    .postSticky(EventData("Song save failed with error: ${e.message}"))
            }

        }
    }

    val song = MediaControllerManager.getSong()
    val fileName = mutableStateOf(song.fileName)
    val title = mutableStateOf(song.title ?: "Unknown")
    val artist = mutableStateOf(song.artist ?: "Unknown")
}