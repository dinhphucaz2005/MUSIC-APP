package nd.phuc.musicapp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import nd.phuc.core.model.LocalSong
import nd.phuc.core.model.ThumbnailSource
import nd.phuc.musicapp.music.domain.repository.LocalSongRepository
import nd.phuc.musicapp.music.presentation.ui.feature.home.HomeViewModel
import nd.phuc.musicapp.util.MediaControllerManager

object FakeModule {

    private val localSongs = listOf(
        LocalSong(
            title = "Song A",
            artist = "Artist X",
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            filePath = "/path/to/songA.mp3",
            durationMillis = 180000,
        ),
        LocalSong(
            title = "Song B",
            artist = "Artist Y",
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            filePath = "/path/to/songB.mp3",
            durationMillis = 200000,
        ),
        LocalSong(
            title = "Song C",
            artist = "Artist Z",
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            filePath = "/path/to/songC.mp3",
            durationMillis = 240000,
        )
    )

    val songRepository = object : LocalSongRepository {
        override val allSongs: Flow<List<LocalSong>>
            get() = TODO("Not yet implemented")
        override val likedSongs: Flow<Set<LocalSong>>
            get() = TODO("Not yet implemented")

        override suspend fun getSongs() {
            TODO("Not yet implemented")
        }
    }

    fun provideHomeViewModel(): HomeViewModel = HomeViewModel(songRepository = songRepository)

    val mediaControllerManager = MediaControllerManager()

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
