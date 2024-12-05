package com.example.musicapp.di

import androidx.compose.runtime.Composable
import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.repository.CloudRepository
import com.example.musicapp.other.domain.repository.SongRepository
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlayListViewModel
import com.example.musicapp.other.viewmodels.SongViewModel
import com.example.musicapp.youtube.presentation.home.YoutubeViewModel
import java.util.UUID

object FakeModule {

    val playlist = PlayList(
        id = UUID.randomUUID().toString(),
        name = "UNNAMED"
    )

    private val songRepository = object : SongRepository {

        override suspend fun getSongsFromPlaylist(playListId: String): List<Song> {
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

        override suspend fun addSongsToPlaylist(playListId: String, songs: List<Song>) {
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

    private val cloudRepository = object : CloudRepository {
        override suspend fun load(): List<Song> {
            TODO("Not yet implemented")
        }

        override fun upload(songs: List<Song>) {
            TODO("Not yet implemented")
        }
    }


    @Composable
    fun providePlaylistViewModel() =
        PlayListViewModel(songRepository)

    @Composable
    fun provideSongViewModel(): SongViewModel =
        SongViewModel(cloudRepository)

    @Composable
    fun provideHomeViewModel(): HomeViewModel =
        HomeViewModel(songRepository)

    @Composable
    fun provideYoutubeViewModel(): YoutubeViewModel =
        YoutubeViewModel()
}