package com.example.musicapp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.SongRepository
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.viewmodels.HomeViewModel
import com.example.musicapp.viewmodels.PlayListViewModel
import com.example.musicapp.viewmodels.SongViewModel
import com.example.musicapp.viewmodels.YoutubeViewModel
import java.util.UUID

object FakeModule {

    val playlist = PlayList(
        id = UUID.randomUUID().toString(),
        name = "UNNAMED"
    )

    @Composable
    fun mediaControllerManager() = MediaControllerManager(LocalContext.current)

    private val songRepository = object : SongRepository {

        override suspend fun getPlayList(playListId: String): List<Song> {
            TODO("Not yet implemented")
        }

        override suspend fun createPlayList(name: String) {
            TODO("Not yet implemented")
        }

        override suspend fun savePlayList(id: String, name: String) {
            TODO("Not yet implemented")
        }

        override suspend fun deletePlayList(id: String) {
            TODO("Not yet implemented")
        }

        override suspend fun addSongs(playListId: String, songs: List<Song>) {
            TODO("Not yet implemented")
        }


        override suspend fun deleteSongs(selectedSongIds: List<String>) {
            TODO("Not yet implemented")
        }

        override suspend fun getLocalSong(): List<Song> {
            TODO("Not yet implemented")
        }

        override suspend fun getPlayLists(): List<PlayList> {
            TODO("Not yet implemented")
        }
    }

    @Composable
    fun provideMainViewModel() =
        SongViewModel(mediaControllerManager())

    @Composable
    fun providePlaylistViewModel() = PlayListViewModel(mediaControllerManager(), songRepository)

    @Composable
    fun provideSongViewModel(): SongViewModel = SongViewModel(mediaControllerManager())

    @Composable
    fun provideHomeViewModel(): HomeViewModel =
        HomeViewModel(mediaControllerManager(), songRepository)

    @Composable
    fun provideYoutubeViewModel(): YoutubeViewModel = YoutubeViewModel(mediaControllerManager())
}