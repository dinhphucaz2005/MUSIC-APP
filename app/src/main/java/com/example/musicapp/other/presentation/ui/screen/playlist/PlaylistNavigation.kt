package com.example.musicapp.other.presentation.ui.screen.playlist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.musicapp.music.presentation.navigation.Routes

fun NavGraphBuilder.playlistNavigation(
    navController: NavHostController,
    playlistViewModel: PlaylistViewModel
) {
    navigation(startDestination = PlaylistRoute.HOME, route = Routes.LOCAL_SONGS) {
        composable(route = PlaylistRoute.HOME) {
            CreateNewPlaylistScreen(
                playlistViewModel = playlistViewModel,
                dismiss = { navController.popBackStack() },
                songs = listOf(),
            )
        }
        composable(route = PlaylistRoute.CREATE_NEW_PLAYLIST) {
            CreateNewPlaylistScreen(
                playlistViewModel = playlistViewModel,
                dismiss = { navController.popBackStack() },
                songs = listOf(),
            )
        }
        composable(
            route = PlaylistRoute.DETAIL + "/{${PlaylistRoute.PLAYLIST_ID}}",
            arguments = listOf(navArgument(PlaylistRoute.PLAYLIST_ID) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getInt(PlaylistRoute.PLAYLIST_ID)
            if (playlistId == null) {
                navController.popBackStack()
                return@composable
            }
            TODO("PlaylistDetailScreen")
        }
        composable(
            route = PlaylistRoute.EDIT + "/{${PlaylistRoute.PLAYLIST_ID}}",
            arguments = listOf(navArgument(PlaylistRoute.PLAYLIST_ID) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getInt(PlaylistRoute.PLAYLIST_ID)
            if (playlistId == null) {
                navController.popBackStack()
                return@composable
            }
//            PlayListEdit(playlistId, navController, playlistViewModel, homeViewModel)
        }
    }
}