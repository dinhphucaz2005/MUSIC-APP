package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innertube.YouTube
import com.example.innertube.models.Artist
import com.example.musicapp.domain.model.AudioSource
import com.example.musicapp.domain.model.Queue
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.model.ThumbnailSource
import com.example.musicapp.ui.components.list
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val mediaControllerManager: MediaControllerManager
) : ViewModel() {
    fun play(index: Int) = viewModelScope.launch {
        try {
            val song = _songs.value[index]

            val audioUrl = YouTube.player(song.id).getOrThrow().streamingData?.adaptiveFormats
                ?.firstOrNull { it.isAudio }?.url ?: return@launch

            val queue = Queue.Builder()
                .setYoutubeSong(
                    listOf(song.copy(audioSource = AudioSource.FromUrl(audioUrl))),
                    UUID.randomUUID().toString()
                )
                .build()

            mediaControllerManager.playQueue(queue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        viewModelScope.launch {
            var result = listOf<Song>()
            YouTube.playlist("PLOzY78Q8ujCLCHBLLOPMx_OhhRi_FP_fs").onSuccess {
                result = it.songs.map { song ->
                    Song(
                        id = song.id,
                        title = song.title,
                        artist = song.artists.joinToString(", ") { artist: Artist -> artist.name },
                        audioSource = AudioSource.FromUrl("EMPTY_URL"),
                        thumbnailSource = ThumbnailSource.FromUrl(song.thumbnail),
                        durationMillis = (song.duration ?: 0) * 1000L,
                    )
                }
            }
            _songs.update { result }
        }
    }

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()


}