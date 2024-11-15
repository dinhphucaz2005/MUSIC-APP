package com.example.musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.innertube.CustomYoutube
import com.example.innertube.models.Artist
import com.example.innertube.models.SongItem
import com.example.innertube.pages.HomePage
import com.example.musicapp.domain.model.AudioSource
import com.example.musicapp.domain.model.Queue
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.load
import com.example.musicapp.util.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _home = MutableStateFlow(HomePage(emptyList()))
    val home: StateFlow<HomePage> = _home.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    fun play(index: Int) = viewModelScope.launch {
        try {
            val song = _songs.value[index]

            val audioUrl = CustomYoutube.player(song.id).getOrThrow().streamingData?.adaptiveFormats
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
        val test = HomePage.Section(
            title = "Section title",
            label = "Section label",
            thumbnail = "url",
            endpoint = null,
            items = listOf(
                SongItem(
                    id = "song id",
                    title = "Song title",
                    artists = listOf(
                        Artist(
                            name = "Song artist",
                            id = "id_artist"
                        )
                    ),
                    album = null,
                    duration = null,
                    thumbnail = "song thumbnail",
                    explicit = false, endpoint = null
                )
            )
        )

        load(_isLoading) {
            _home.update {
                HomePage(
                    listOf(
                        test,
                        test.copy(title = "Forgotten favorites", label = null),
                        test.copy(title = "Mixed for you", label = null)
                    )
                )
            }
            _home.update { CustomYoutube.loadHome() }
        }
    }
}
