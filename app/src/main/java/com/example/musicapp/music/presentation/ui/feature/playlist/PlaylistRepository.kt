package com.example.musicapp.music.presentation.ui.feature.playlist

import com.example.musicapp.di.FakeModule
import com.example.musicapp.music.domain.model.Playlist
import com.example.musicapp.music.domain.model.Song
import javax.inject.Inject
import kotlin.random.Random

class PlaylistRepository @Inject constructor() {
    fun getPlaylist(string: String) = Playlist(
        Random.nextInt(), string, songs = listOf(
            FakeModule.localSong,
            FakeModule.localSong,
            FakeModule.localSong,
            FakeModule.localSong,
            FakeModule.localSong,
        )
    )

    fun getPlaylistSongs(string: String) = listOf<Song>(
        FakeModule.localSong,
        FakeModule.localSong,
        FakeModule.localSong,
        FakeModule.localSong,
        FakeModule.localSong,
    )

    fun createPlaylist(name: String, description: String, thumbnailUrl: String?) {}

}
