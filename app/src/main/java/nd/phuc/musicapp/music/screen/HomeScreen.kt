package nd.phuc.musicapp.music.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import nd.phuc.core.presentation.previews.ExtendDevicePreviews
import nd.phuc.core.presentation.theme.MyMusicAppTheme
import nd.phuc.musicapp.di.fakeViewModel
import nd.phuc.musicapp.music.home.HomeViewModel
import nd.phuc.musicapp.music.home.components.YourSongsSection


@ExperimentalMaterial3Api
@UnstableApi
@ExtendDevicePreviews
@Composable
private fun Preview() {
    MyMusicAppTheme {
        HomeScreen(homeViewModel = fakeViewModel<HomeViewModel>())
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
) {
    val songs by homeViewModel.songs.collectAsState()

    YourSongsSection(
        songs = songs,
        modifier = modifier.fillMaxSize()
    )

}
