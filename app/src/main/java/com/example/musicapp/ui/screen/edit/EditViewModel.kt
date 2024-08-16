package com.example.musicapp.ui.screen.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.domain.repository.SongFileRepository
import com.example.musicapp.extension.getFileNameWithoutExtension
import com.example.musicapp.util.EventData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class EditViewModel @Inject constructor(
    private val editSongRepository: EditSongRepository,
    private val songFileRepository: SongFileRepository,
) : ViewModel() {

    companion object {
        const val TAG = "EditViewModel"
    }

    fun saveSongFile(
        fileName: String,
        title: String?,
        artist: String?,
        imageUri: Uri?,
        song: Song,
        onSaved: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            if (fileName == song.fileName.getFileNameWithoutExtension()) {
                EventBus.getDefault().postSticky(EventData("File name already exist"))
                return@launch
            }
            editSongRepository.saveSongFile(
                fileName, title, artist, imageUri, song,
                object : ResultCallback<String> {
                    override fun onSuccess(result: String) {
                        songFileRepository.reload()
                        onSaved?.invoke()
                        EventBus.getDefault().postSticky(EventData(result))
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
}
