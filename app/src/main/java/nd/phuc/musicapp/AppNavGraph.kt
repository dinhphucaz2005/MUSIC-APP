package nd.phuc.musicapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nd.phuc.core.extension.appHiltViewModel
import nd.phuc.core.extension.routeComposable
import nd.phuc.musicapp.music.presentation.ui.feature.home.HomeViewModel
import nd.phuc.musicapp.music.presentation.ui.feature.home.screen.HomeScreen
import nd.phuc.musicapp.music.presentation.ui.feature.library.LibraryScreen
import nd.phuc.musicapp.music.presentation.ui.feature.playlists.PlaylistScreen
import nd.phuc.musicapp.music.presentation.ui.feature.playlists.PlaylistsViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        routeComposable(
            route = Screens.Home,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            exitTransition = { slideOutHorizontally { -it / 2 } + fadeOut() },
            popEnterTransition = { slideInHorizontally { -it / 2 } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            HomeScreen(homeViewModel = appHiltViewModel<HomeViewModel>())
        }

        composable(
            route = Screens.Playlists.route,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            exitTransition = { slideOutHorizontally { -it / 2 } + fadeOut() }
        ) {
            PlaylistScreen(playlistsViewModel = appHiltViewModel<PlaylistsViewModel>())
        }

        composable(
            route = Screens.Library.route,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) }
        ) {
            LibraryScreen()
        }
    }
}
