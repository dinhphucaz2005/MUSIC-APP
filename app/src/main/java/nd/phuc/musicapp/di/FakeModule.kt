package nd.phuc.musicapp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.Playlist
import nd.phuc.core.domain.model.ThumbnailSource
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import nd.phuc.musicapp.music.viewmodel.HomeViewModel
import nd.phuc.musicapp.music.viewmodel.PlaylistsViewModel
import org.koin.androidx.compose.koinViewModel

object FakeModule {

    private val localSongs = listOf(
        LocalSong(
            title = "Song A",
            artist = "Artist X",
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            filePath = "/path/to/songA.mp3",
            durationMillis = 180000,
            isLiked = false,
        ),
        LocalSong(
            title = "Song B",
            artist = "Artist Y",
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            filePath = "/path/to/songB.mp3",
            durationMillis = 200000,
            isLiked = true,
        ),
        LocalSong(
            title = "Song C",
            artist = "Artist Z",
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            filePath = "/path/to/songC.mp3",
            durationMillis = 240000,
            isLiked = false,
        )
    )

    private val localPlaylist = listOf(
        Playlist(
            id = 1L,
            name = "My Favorites",
            songs = localSongs,
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
        )
    )

    val songRepository = object : LocalSongRepository {
        override val allSongs: Flow<List<LocalSong>> = flow {
            emit(localSongs)
        }
        override val playlist: Flow<List<Playlist<LocalSong>>> = flow {
            emit(localPlaylist)
        }
        override val likedSongs: Flow<List<LocalSong>>
            get() = TODO("Not yet implemented")

        override suspend fun toggleLike(value: LocalSong) {
            // No-op for fake implementation
        }

        override suspend fun getSongs() {
            // No-op for fake implementation
        }

        override suspend fun createPlaylist(
            name: String,
            songs: List<LocalSong>,
        ) {
            // No-op for fake implementation
        }

        override suspend fun addSongToPlaylist(
            playlistId: Long,
            song: LocalSong,
        ) {
            // No-op for fake implementation
        }
    }

    fun provideHomeViewModel(): HomeViewModel =
        HomeViewModel(songRepository = songRepository)

    fun providePlaylistsViewModel(): PlaylistsViewModel =
        PlaylistsViewModel(songRepository = songRepository)
}

@Composable
inline fun <reified T : ViewModel> fakeViewModel(): T {
    val isPreview = LocalInspectionMode.current
    return if (isPreview) {
        when (T::class) {
            HomeViewModel::class -> FakeModule.provideHomeViewModel()
            PlaylistsViewModel::class -> FakeModule.providePlaylistsViewModel()
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    } else {
        koinViewModel()
    }
}
