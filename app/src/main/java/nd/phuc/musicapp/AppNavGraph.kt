package nd.phuc.musicapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import nd.phuc.musicapp.music.LibraryScreen
import nd.phuc.musicapp.music.screen.HomeScreen
import nd.phuc.musicapp.music.playlists.PlaylistScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(backStack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backStack,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
        ),
        entryProvider = { key ->
            if (key is Screens) {
                return@NavDisplay when (key) {
                    Screens.Home -> NavEntry(key = Screens.Home) {
                        HomeScreen(homeViewModel = koinViewModel())
                    }

                    Screens.Library -> NavEntry(key = Screens.Library) {
                        LibraryScreen()
                    }

                    Screens.Playlists -> NavEntry(key = Screens.Playlists) {
                        PlaylistScreen(playlistsViewModel = koinViewModel())
                    }

                }
            } else {
                throw Exception("Unknow Error")
            }
        }
    )
}
