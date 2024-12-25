package com.example.musicapp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.innertube.models.AlbumItem
import com.example.innertube.models.Artist
import com.example.innertube.models.ArtistItem
import com.example.innertube.models.SongItem
import com.example.innertube.pages.AlbumPage
import com.example.innertube.pages.ArtistPage
import com.example.innertube.pages.ArtistSection
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.ThumbnailSource
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

    private fun provideSongRepository(): SongRepository = object : SongRepository {

        override suspend fun getSongsFromPlaylist(playListId: String): List<LocalSong> = emptyList()
        override suspend fun getSongByPath(path: String): LocalSong? = null

        override suspend fun createPlayList(name: String) = Unit

        override suspend fun savePlayList(id: String, name: String) = Unit

        override suspend fun deletePlayList(id: String) = Unit
        override suspend fun addSongsToPlaylist(playListId: String, localSongs: List<LocalSong>) =
            Unit

        override suspend fun deleteSongs(selectedSongIds: List<String>) = Unit

        override suspend fun getLocalSong(): List<LocalSong> = emptyList()

        override suspend fun getPlayLists(): List<PlayList> = emptyList()
        override suspend fun likeSong(song: Song) {
            TODO("Not yet implemented")
        }

        override suspend fun unlikeSong(id: Long) = Unit

        override suspend fun getLikedSongs(): List<Song> = emptyList()
    }


    @Composable
    fun providePlaylistViewModel() =
        PlaylistViewModel(provideSongRepository())

    @Composable
    fun provideSongViewModel(): SongViewModel =
        SongViewModel()

    @Composable
    fun provideHomeViewModel(): HomeViewModel =
        HomeViewModel(provideSongRepository())

    @Composable
    fun provideYoutubeViewModel(): YoutubeViewModel = YoutubeViewModel()

    @Composable
    fun provideMediaControllerManager(): MediaControllerManager {
        val context = LocalContext.current
        return MediaControllerManager(context, null)
    }

    fun provideSong(): LocalSong = LocalSong(
        id = "1",
        title = "Đã Từng Hạnh Phúc Remix | Nhạc Mix Gây Nghiện 2019 | Music Time",
        artist = "Music Time",
        audio = android.net.Uri.EMPTY,
        thumbnailSource = ThumbnailSource.FromBitmap(null),
        durationMillis = (4 * 60 + 38) * 1000
    )

    fun provideArtistPage(): ArtistPage {
        return ArtistPage(
            artist = ArtistItem(UUID.randomUUID().toString(), "Music Time", "", null, null),
            sections = listOf(
                ArtistSection(
                    "Music Time",
                    items = listOf(provideSongItem()),
                    moreEndpoint = null,
                ),
                ArtistSection(
                    "Music Time",
                    items = listOf(provideAlbumItem()),
                    moreEndpoint = null,
                )
            ),
            description = "Music Time"
        )
    }

    fun provideAlbumPage(): AlbumPage = AlbumPage(
        album = AlbumItem(
            browseId = UUID.randomUUID().toString(),
            playlistId = UUID.randomUUID().toString(),
            id = UUID.randomUUID().toString(),
            title = "Đã Từng Hạnh Phúc Remix | Nhạc Mix Gây Nghiện 2019 | Music Time",
            artists = listOf(Artist("Music Time", null)),
            year = null,
            thumbnail = "",
            explicit = false
        ),
        songs = List(20) { provideSongItem() },
        otherVersions = emptyList()
    )

    private fun provideAlbumItem(): AlbumItem {
       return AlbumItem(
            browseId = UUID.randomUUID().toString(),
            playlistId = UUID.randomUUID().toString(),
            id = UUID.randomUUID().toString(),
            title = "Đã Từng Hạnh Phúc Remix | Nhạc Mix G",
            artists = listOf(Artist("Music Time", null)),
            year = null,
            thumbnail = "",
            explicit = false
        )
    }

    private fun provideSongItem(): SongItem {
        return SongItem(
            id = UUID.randomUUID().toString(),
            title = "Đã Từng Hạnh Phúc Remix | Nhạc Mix Gây Nghiện 2019 | Music Time",
            artists = listOf(Artist("Music Time", null)),
            album = null,
            duration = null,
            thumbnail = "",
            explicit = false,
            endpoint = null,
        )
    }
}