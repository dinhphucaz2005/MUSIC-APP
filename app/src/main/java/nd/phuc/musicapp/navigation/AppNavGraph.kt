package nd.phuc.musicapp.navigation

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
import nd.phuc.musicapp.music.screen.LibraryScreen
import nd.phuc.musicapp.music.screen.PlaylistDetailScreen
import nd.phuc.musicapp.music.screen.HomeScreen
import nd.phuc.musicapp.music.screen.PlaylistScreen
import nd.phuc.musicapp.navigation.Screens.*
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(
    backStack: NavBackStack<NavKey>,
    onNavigate: (Screens) -> Unit,
    onBack: () -> Unit,
) {
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
                    Home -> NavEntry(key = key) {
                        HomeScreen(
                            homeViewModel = koinViewModel(),
                            onNavigateToPlayer = { /* Player is now an overlay in AppScreen */ })
                    }

                    Library -> NavEntry(key = key) {
                        LibraryScreen()
                    }

                    Playlists -> NavEntry(key = key) {
                        PlaylistScreen(
                            playlistsViewModel = koinViewModel(), onPlaylistClick = { playlistId ->
                                onNavigate(PlaylistDetail(playlistId))
                            })
                    }

                    is PlaylistDetail -> NavEntry(key = key) {
                        PlaylistDetailScreen(
                            playlistId = key.playlistId,
                            onBackClick = onBack,
                            onNavigateToPlayer = { /* Player is now an overlay in AppScreen */ },
                            playlistsViewModel = koinViewModel()
                        )
                    }


                }
            } else {
                throw Exception("Unknow Error")
            }
        })
}


