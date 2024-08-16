package com.example.musicapp.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.SongFileRepository
import com.example.musicapp.enums.PlayingState
import com.example.musicapp.enums.PlaylistState
import com.example.musicapp.extension.getFileNameWithoutExtension
import com.example.musicapp.util.MediaControllerManager
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
    private val mediaControllerManager: MediaControllerManager,
) : ViewModel() {

    private val _isPlaying = MutableStateFlow(PlayingState.FALSE)
    private val _bitmap = MutableStateFlow<ImageBitmap?>(null)
    private val _title = MutableStateFlow("NO SONG FOUND")
    private val _artist = MutableStateFlow("NO ARTIST FOUND")
    private val _playlistState = MutableStateFlow(PlaylistState.REPEAT_ALL)
    private val _duration = MutableStateFlow("(>.<)")

    fun getCurrentSong(): Song? {
        val index = mediaControllerManager.getCurrentSongIndex() ?: return null
        return songList[index]
    }

    fun getTitle(): StateFlow<String> = _title
    fun getArtist(): StateFlow<String> = _artist
    fun getThumbnail(): StateFlow<ImageBitmap?> = _bitmap
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
                mediaControllerManager.currentSong.collect { _ ->
                    val currentSong = getCurrentSong() ?: return@collect
                    _bitmap.value = currentSong.thumbnail
                    _title.value =
                        currentSong.title ?: currentSong.fileName.getFileNameWithoutExtension()
                    _artist.value = currentSong.artist ?: "NO ARTIST FOUND"
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

    fun refreshPlayListState() {
        mediaControllerManager.updatePlayListState()
    }

    fun skipToNextTrack() {
        mediaControllerManager.playNextSong()
    }

    fun playPreviousTrack() {
        mediaControllerManager.playPreviousSong()
    }

    fun rewind() {
        mediaControllerManager.adjustPlaybackByOffset(-5000)
    }

    fun fastForward() {
        mediaControllerManager.adjustPlaybackByOffset(5000)
    }


    fun playTrackAtIndex(index: Int) {
        mediaControllerManager.playSongAtIndex(index)
    }

    fun togglePlayPause() {
        mediaControllerManager.togglePlayPause()
    }

    fun seekToPosition(sliderPosition: Float) { //!warning: 0 <= sliderPosition <= 1
        mediaControllerManager.seekToPosition(sliderPosition)
    }

    @Deprecated("No longer used")
    fun fetchCurrentPosition(): Float {
        return mediaControllerManager.getCurrentPlaybackPosition()
    }

    @Deprecated("No longer used")
    fun performSearch(searchQuery: String) {
        viewModelScope.launch {
            Log.d(TAG, "search: $searchQuery")
            songList.clear()
            repository.search(searchQuery)?.let { songList.addAll(it) }
        }
    }

    @Deprecated("No longer used")
    fun reloadRepository() {
        viewModelScope.launch {
            repository.reload()
        }
    }
}