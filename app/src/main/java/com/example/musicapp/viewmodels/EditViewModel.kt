package com.example.musicapp.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.util.EventData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class EditViewModel @Inject constructor(
    private val editSongRepository: EditSongRepository,
    private val playlistRepository: PlayListRepository
) : ViewModel() {

    companion object {
        private const val TAG = "EditViewModel"
    }

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    fun saveSongFile(
        fileName: String,
        title: String?,
        artist: String?,
        imageUri: Uri?,
        song: Song,
        onSaved: (() -> Unit)? = null
    ) {
        if (fileName == song.getFileName()) {
            EventBus.getDefault().postSticky(EventData("File name already exist"))
            return
        }
        viewModelScope.launch {
            editSongRepository.saveSongFile(
                fileName,
                title,
                artist,
                imageUri,
                song,
                object : ResultCallback<String> {
                    override fun onSuccess(result: String) {
                        CoroutineScope(Dispatchers.IO).launch {
                            playlistRepository.reload()
                        }
//                        onSaved?.invoke()
                        _message.update { result }
                        Log.d(TAG, "onSuccess: $result")
                    }

                    override fun onFailure(exception: Exception) {
                        exception.message?.let {
                        }
                    }
                })
        }
    }
}
