package com.example.musicapp.other.presentation.ui.screen.playlist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.musicapp.constants.Screens
import com.example.musicapp.other.presentation.ui.screen.playlist.PlaylistRoute.PLAYLIST_ID
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel

fun NavGraphBuilder.playlistNavigation(
    navController: NavHostController,
    playlistViewModel: PlaylistViewModel,
    homeViewModel: HomeViewModel
) {
    navigation(startDestination = PlaylistRoute.HOME, route = Screens.Playlists.route) {
        composable(route = PlaylistRoute.HOME) {
            PlayListHome(playlistViewModel = playlistViewModel)
        }
        composable(
            route = PlaylistRoute.DETAIL + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getInt(PLAYLIST_ID)
            if (playlistId == null) {
                navController.popBackStack()
                return@composable
            }
            TODO("PlaylistDetailScreen")
        }
        composable(
            route = PlaylistRoute.EDIT + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getInt(PLAYLIST_ID)
            if (playlistId == null) {
                navController.popBackStack()
                return@composable
            }
            PlayListEdit(playlistId, navController, playlistViewModel, homeViewModel)
        }
    }
}