package com.example.musicapp.util

import android.net.Uri
import com.example.musicapp.music.domain.model.CurrentSong
import com.example.musicapp.music.domain.model.LocalSong
import com.example.musicapp.music.domain.model.PlayBackState
import com.example.musicapp.music.domain.model.Queue
import com.example.musicapp.music.domain.model.Song
import com.example.musicapp.music.domain.model.SongId
import com.example.musicapp.music.domain.model.ThumbnailSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MediaControllerManager {

    val audioSessionId: StateFlow<Int?>
    val playBackState: StateFlow<PlayBackState>
    val currentSong: StateFlow<CurrentSong>
    val queue: StateFlow<Queue?>

    fun playQueue(songs: List<Song>, index: Int = 0, id: String): Unit?

    fun computePlaybackFraction(): Float?

    fun getCurrentTrackPosition(): Long?

    fun downLoadCurrentSong()

    fun seekToSliderPosition(sliderPosition: Float)

    fun updatePlayListState(): Unit?

    fun playPreviousSong(): Unit?

    fun togglePlayPause(): Unit?

    fun playNextSong(): Unit?

    fun getCurrentMediaIndex(): Int?

    fun playAtIndex(index: Int): Unit?

    fun toggleLikedCurrentSong()

    fun addToNext(song: Song)

    fun addToQueue(song: Song)

    fun dispose()
}

class UninitializedMediaControllerManager : MediaControllerManager {
    override val audioSessionId: StateFlow<Int?>
        get() = MutableStateFlow<Int?>(null).asStateFlow()

    override val playBackState: StateFlow<PlayBackState>
        get() = MutableStateFlow<PlayBackState>(PlayBackState()).asStateFlow()

    override val currentSong: StateFlow<CurrentSong> =
        MutableStateFlow<CurrentSong>(
            CurrentSong(
                data = LocalSong(
                    id = SongId.Local(""),
                    title = "",
                    artist = "",
                    uri = Uri.EMPTY,
                    thumbnailSource = ThumbnailSource.FromUrl(""),
                    durationMillis = 0
                ),
                isLiked = false
            )
        ).asStateFlow()

    override val queue: StateFlow<Queue?> = MutableStateFlow<Queue?>(null).asStateFlow()

    override fun playQueue(
        songs: List<Song>,
        index: Int,
        id: String
    ) {
    }

    override fun computePlaybackFraction(): Float? = null

    override fun getCurrentTrackPosition(): Long? = null

    override fun downLoadCurrentSong() {}

    override fun seekToSliderPosition(sliderPosition: Float) {}

    override fun updatePlayListState() {}

    override fun playPreviousSong() {}

    override fun togglePlayPause() {}

    override fun playNextSong() {}

    override fun getCurrentMediaIndex(): Int? = null

    override fun playAtIndex(index: Int) {}

    override fun toggleLikedCurrentSong() {}

    override fun addToNext(song: Song) {}

    override fun addToQueue(song: Song) {}

    override fun dispose() {}
}