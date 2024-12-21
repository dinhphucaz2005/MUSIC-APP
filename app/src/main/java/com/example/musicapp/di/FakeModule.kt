package com.example.musicapp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.musicapp.other.domain.model.AudioSource
import com.example.musicapp.other.domain.model.PlayBackState
import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.other.domain.repository.CloudRepository
import com.example.musicapp.other.domain.repository.SongRepository
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import com.example.musicapp.other.viewmodels.SongViewModel
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.youtube.presentation.YoutubeViewModel
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
        PlaylistViewModel(songRepository)

    @Composable
    fun provideSongViewModel(): SongViewModel =
        SongViewModel(cloudRepository)

    @Composable
    fun provideHomeViewModel(): HomeViewModel =
        HomeViewModel(songRepository)

    @Composable
    fun provideYoutubeViewModel(): YoutubeViewModel =
        YoutubeViewModel()

    @Composable
    fun provideMediaControllerManager(): MediaControllerManager {
        val context = LocalContext.current
        return MediaControllerManager(context, null)
    }

    fun providePlayBackState(): PlayBackState = PlayBackState()

    @Composable
    fun provideSong(): Song = Song(
        id = "1",
        title = "Đã Từng Hạnh Phúc Remix | Nhạc Mix Gây Nghiện 2019 | Music Time",
        artist = "Music Time",
        audioSource = AudioSource.FromUrl("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
        thumbnailSource = ThumbnailSource.FromBitmap(null),
        durationMillis = (4 * 60 + 38) * 1000
    )
}