package com.example.musicapp.util

import com.example.innertube.models.SongItem
import com.example.musicapp.other.domain.model.CurrentSong
import com.example.musicapp.other.domain.model.PlayBackState
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import kotlinx.coroutines.flow.StateFlow

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

    fun playYoutubeSong(songItem: SongItem)

    fun addToNext(song: Song)

    fun addToQueue(song: Song)
}