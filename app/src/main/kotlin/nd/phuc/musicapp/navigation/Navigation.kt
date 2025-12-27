package nd.phuc.musicapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nd.phuc.musicapp.ui.main.ArtistScreen
import nd.phuc.musicapp.ui.main.PlaylistDetailScreen
import nd.phuc.musicapp.ui.main.PlaylistScreen
import nd.phuc.musicapp.ui.songs.SongsScreen

object Destinations {
    const val HOME = "home"
    const val PLAYLIST = "playlist"
    const val ARTIST = "artist"
    const val PLAYLIST_DETAIL = "playlist/{playlistId}"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.HOME,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.HOME) { SongsScreen() }
        composable(Destinations.PLAYLIST) { PlaylistScreen() }
        composable(Destinations.ARTIST) { ArtistScreen() }
        composable("playlist/{playlistId}") { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId") ?: ""
            PlaylistDetailScreen(playlistId)
        }
    }
}
