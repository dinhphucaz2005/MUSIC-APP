package nd.phuc.musicapp.music.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.UnstableApi
import nd.phuc.core.presentation.theme.MyMusicAppTheme
import nd.phuc.musicapp.di.fakeViewModel
import nd.phuc.musicapp.music.viewmodel.HomeViewModel
import nd.phuc.musicapp.music.home.components.YourSongsSection
import nd.phuc.musicapp.LocalMediaControllerManager


@ExperimentalMaterial3Api
@UnstableApi
@Preview
@Composable
private fun Preview() {
    MyMusicAppTheme {
        HomeScreen(
            homeViewModel = fakeViewModel<HomeViewModel>(),
            onNavigateToPlayer = {}
        )
    }
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToPlayer: () -> Unit,
) {
    val mediaControllerManager = LocalMediaControllerManager.current
    val songs by homeViewModel.songs.collectAsState()

    YourSongsSection(
        songs = songs,
        modifier = Modifier.fillMaxSize(),
        onSongClick = { song ->
            mediaControllerManager.play(song)
            onNavigateToPlayer()
        }
    )

}
