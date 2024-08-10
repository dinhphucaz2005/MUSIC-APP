package com.example.mymusicapp.ui.screen.edit

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.mymusicapp.callback.ResultCallback
import com.example.mymusicapp.di.AppModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.EditSongRepository
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.helper.MediaStoreHelper
import com.example.mymusicapp.util.EventData
import com.example.mymusicapp.util.MediaControllerManager
import com.mpatric.mp3agic.Mp3File
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min

@UnstableApi
@HiltViewModel
class EditViewModel @Inject constructor(
    private val editSongRepository: EditSongRepository,
    private val songFileRepository: SongFileRepository,
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {

    companion object {
        const val TAG = "EditViewModel"
    }

    @SuppressLint("Recycle")
    fun saveSongFile(uri: Uri) {
        viewModelScope.launch {
            editSongRepository.saveSongFile(
                songFilePath = "",
                imageUri = uri,
                title = "value",
                artist = "value",
                newFileName = "fileName.value",
                object : ResultCallback<String> {
                    override fun onSuccess(result: String) {
                        songFileRepository.reload()
                    }

                    override fun onFailure(exception: Exception) {
                        exception.message?.let {
                            EventBus.getDefault().postSticky(EventData(it))
                        }
                    }
                }
            )
        }
    }

//    val song = mediaControllerManager.currentSong.value
//    val fileName = mutableStateOf(song.fileName)
//    val title = mutableStateOf(song.title ?: "Unknown")
//    val artist = mutableStateOf(song.artist ?: "Unknown")
}
