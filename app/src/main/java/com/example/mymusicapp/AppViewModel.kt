package com.example.mymusicapp

import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.enums.PlayingState
import com.example.mymusicapp.enums.PlaylistState
import com.example.mymusicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: SongFileRepository,
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {

    private val _isPlaying = MutableStateFlow(PlayingState.FALSE)
    private val _bitmap = MutableStateFlow<ImageBitmap?>(null)
    private val _title = MutableStateFlow("NO SONG FOUND")
    private val _artist = MutableStateFlow("NO ARTIST FOUND")
    private val _playlistState = MutableStateFlow(PlaylistState.REPEAT_ALL)
    private val _duration = MutableStateFlow("(>.<)")

    fun getTitle(): StateFlow<String> = _title
    fun getArtist(): StateFlow<String> = _artist
    fun getBitmap(): StateFlow<ImageBitmap?> = _bitmap
    fun isPlaying(): StateFlow<PlayingState> = _isPlaying
    fun getDuration(): StateFlow<String> = _duration
    fun getPlayListState(): StateFlow<PlaylistState> = _playlistState

    val songList = mutableStateListOf<Song>()

    companion object {
        private const val TAG = "AppViewModel"
    }

    init {
        Log.d(TAG, "HomeViewModel: init")
        viewModelScope.launch {
            launch {
                repository.getLocal().collect {
                    songList.clear()
                    songList.addAll(it)
                }
            }
            launch {
                mediaControllerManager.currentSong.collect { mediaMetadata ->
                    mediaMetadata?.title?.let {
                        _title.value = it.toString()
                    }
                    mediaMetadata?.artist?.let {
                        _artist.value = it.toString()
                    }
                    mediaMetadata?.artworkData?.let {
                        _bitmap.value =
                            BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
                    }
                }
            }
            launch {
                mediaControllerManager.isPlaying.collect {
                    _isPlaying.value = PlayingState.fromBoolean(it)
                }
            }
            launch {
                mediaControllerManager.playListState.collect {
                    _playlistState.value = it
                }
            }
            launch {
                mediaControllerManager.duration.collect { durationMs ->
                    durationMs?.let {
                        if (durationMs >= 0) {
                            val durationFormatted = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                durationMs / 60000,
                                (durationMs % 60000) / 1000
                            )
                            _duration.value = durationFormatted
                        }
                    }
                }
            }
        }
    }

    fun updatePlayListState() {
        mediaControllerManager.updatePlayListState()
    }

    fun playNext() {
        mediaControllerManager.playNext()
    }

    fun search(searchQuery: String) {
        viewModelScope.launch {
            Log.d(TAG, "search: $searchQuery")
            songList.clear()
            repository.search(searchQuery)?.let { songList.addAll(it) }
        }
    }

    fun reload() {
        viewModelScope.launch {
            repository.reload()
        }
    }

    fun playIndex(index: Int) {
        mediaControllerManager.playIndex(index)
    }

    fun playOrPause() {
        mediaControllerManager.playOrPause()
    }


    fun playBack() {
        mediaControllerManager.playPrevious()
    }

    fun seekTo(sliderPosition: Float) { //!warning: 0 <= sliderPosition <= 1
        println("sliderPosition: $sliderPosition")
        mediaControllerManager.seekTo(sliderPosition)
    }

    fun getCurrentPosition(): Float {
        return mediaControllerManager.getCurrentPosition()
    }

}