package com.example.musicapp.other.presentation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.musicapp.other.presentation.ui.navigation.RouteConstants.PLAYLIST_ID
import com.example.musicapp.other.presentation.ui.screen.playlist.PlayListDetail
import com.example.musicapp.other.presentation.ui.screen.playlist.PlayListEdit
import com.example.musicapp.other.presentation.ui.screen.playlist.PlayListHome
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import com.example.musicapp.song.presetation.components.Screens

fun NavGraphBuilder.playlistNavigation(
    navController: NavHostController,
    playlistViewModel: PlaylistViewModel,
    homeViewModel: HomeViewModel
) {
    navigation(startDestination = Screens.PlaylistHome.route, route = Screens.Playlists.route) {
        composable(route = Screens.PlaylistHome.route) {
            PlayListHome(navController, playlistViewModel)
        }
        composable(
            route = Screens.PlaylistDetail.route + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getString(PLAYLIST_ID)
            PlayListDetail(playlistId, navController, playlistViewModel)
        }
        composable(
            route = Screens.PlaylistEdit.route + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getString(PLAYLIST_ID)
            PlayListEdit(playlistId ?: "", navController, playlistViewModel, homeViewModel)
        }
    }
}