package com.example.musicapp.di

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.callback.AppResource
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.ui.MainViewModel
import com.example.musicapp.ui.screen.song.EditViewModel
import com.example.musicapp.ui.screen.playlist.PlaylistViewModel
import com.example.musicapp.ui.screen.playlist.SelectSongViewModel
import com.example.musicapp.util.MediaControllerManager
import kotlinx.coroutines.flow.StateFlow

object FakeModule {

    val playlist = Playlist(
        id = 0,
        name = "UNNAMED",
        songs = List(20) { Song(fileName = "Anh da xa em that roi ${it}.mp3") }.toMutableList()
    )

    private val editSongRepository = object : EditSongRepository {
        override suspend fun saveSongFile(
            fileName: String,
            title: String?,
            artist: String?,
            imageUri: Uri?,
            song: Song,
            onSaveFile: ResultCallback<String>
        ) {
            TODO("Not yet implemented")
        }

    }

    @Composable
    fun mediaControllerManager() = MediaControllerManager(LocalContext.current)


    private val playlistRepository = object : PlaylistRepository {
        override suspend fun reload(): AppResource<Nothing> {
            TODO("Not yet implemented")
        }

        override suspend fun addPlaylist(name: String) {
            TODO("Not yet implemented")
        }

        override suspend fun savePlaylist(id: Long, name: String, songs: List<Song>) {
            TODO("Not yet implemented")
        }


        override fun observeCurrentPlaylist(): StateFlow<Playlist?> {
            TODO("Not yet implemented")
        }

        override fun observeLocalPlaylist(): StateFlow<Playlist?> {
            TODO("Not yet implemented")
        }

        override fun observeAllPlaylistsFromDatabase(): StateFlow<List<Playlist>> {
            TODO("Not yet implemented")
        }

        override suspend fun deleteSongs(deleteSongIndex: MutableList<Int>, id: Long) {
            TODO("Not yet implemented")
        }

        override suspend fun deletePlaylist(id: Long) {
            TODO("Not yet implemented")
        }

        override fun setLocal(index: Int) {
            TODO("Not yet implemented")
        }

        override fun setPlaylist(playlistId: Long, index: Int) {
            TODO("Not yet implemented")
        }

    }

    @Composable
    @UnstableApi
    fun provideViewModel() = MainViewModel(playlistRepository, mediaControllerManager())

    @Composable
    @OptIn(UnstableApi::class)
    fun provideEditViewModel() = EditViewModel(editSongRepository, playlistRepository)

    @Composable
    @UnstableApi
    fun providePlaylistViewModel() = PlaylistViewModel(playlistRepository, mediaControllerManager())

    fun provideSelectSongViewModel() = SelectSongViewModel(playlistRepository)

}