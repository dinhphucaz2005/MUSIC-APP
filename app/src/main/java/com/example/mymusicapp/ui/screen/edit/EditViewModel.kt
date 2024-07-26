package com.example.mymusicapp.ui.screen.edit

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.util.MediaControllerManager
import com.mpatric.mp3agic.Mp3File
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream

@UnstableApi
class EditViewModel : ViewModel() {
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
                        val outputStream = ByteArrayOutputStream()
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                        id3v2Tag.setAlbumImage(
                            outputStream.toByteArray(), "image/jpeg"
                        )
                    }
                }
                mp3File.save("/storage/emulated/0/Music/${fileName.value}.mp3")
                message.value = "Song saved successfully"
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    val song = MediaControllerManager.getSong()

    val message = mutableStateOf("")
    val fileName = mutableStateOf(song.title)
    val title = mutableStateOf(song.title)
    val artist = mutableStateOf(song.artist)

}