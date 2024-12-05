package com.example.musicapp.youtube.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.musicapp.other.presentation.ui.navigation.RouteConstants.PLAYLIST_ID
import com.example.musicapp.song.presetation.components.Screens
import com.example.musicapp.youtube.presentation.home.YoutubeScreen
import com.example.musicapp.youtube.presentation.playlist.PlaylistDetail
import com.example.musicapp.youtube.presentation.search.YoutubeSearchScreen
import com.example.musicapp.youtube.presentation.search.YoutubeSearchViewModel

fun NavGraphBuilder.youtubeNavigation(
    navController: NavHostController,
    youtubeViewModel: YoutubeViewModel,
) {
    navigation(startDestination = Screens.YoutubeHome.route, route = Screens.Youtube.route) {
        composable(Screens.YoutubeHome.route) {
            YoutubeScreen(
                navController = navController, youtubeViewModel = youtubeViewModel
            )
        }
        composable(
            route = Screens.YoutubePlaylistDetail.route + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId =
                navBackStackEntry.arguments?.getString(PLAYLIST_ID) ?: return@composable
            PlaylistDetail(playlistId, navController, youtubeViewModel)
        }
        composable(Screens.YoutubeSearch.route) {
            YoutubeSearchScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = hiltViewModel<YoutubeSearchViewModel>()
            )
        }
    }
}