package nd.phuc.musicapp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.Playlist
import nd.phuc.core.domain.model.ThumbnailSource
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import nd.phuc.musicapp.music.presentation.ui.feature.home.HomeViewModel

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

    val songRepository = object : LocalSongRepository {
        override val allSongs: Flow<List<LocalSong>> = flow {
            emit(localSongs)
        }
        override val playlist: Flow<List<Playlist<LocalSong>>> = flow {
            emit(emptyList())
        }

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
    }

    fun provideHomeViewModel(): HomeViewModel = HomeViewModel(songRepository = songRepository)

}

@Composable
inline fun <reified T : ViewModel> fakeViewModel(): T {
    val isPreview = LocalInspectionMode.current
    return if (isPreview) {
        when (T::class) {
            HomeViewModel::class -> FakeModule.provideHomeViewModel()
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    } else hiltViewModel<T>()
}
