package com.example.musicapp.other.presentation.ui.screen.playlist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.musicapp.other.presentation.ui.screen.playlist.PlaylistRoute.PLAYLIST_ID
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import com.example.musicapp.song.presetation.components.Screens

fun NavGraphBuilder.playlistNavigation(
    navController: NavHostController,
    playlistViewModel: PlaylistViewModel,
    homeViewModel: HomeViewModel
) {
    navigation(startDestination = PlaylistRoute.HOME, route = Screens.Playlists.route) {
        composable(route = PlaylistRoute.HOME) {
            PlayListHome(navController, playlistViewModel)
        }
        composable(
            route = PlaylistRoute.DETAIL + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getString(PLAYLIST_ID)
            PlayListDetail(playlistId, navController, playlistViewModel)
        }
        composable(
            route = PlaylistRoute.EDIT + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getString(PLAYLIST_ID)
            PlayListEdit(playlistId ?: "", navController, playlistViewModel, homeViewModel)
        }
    }
}