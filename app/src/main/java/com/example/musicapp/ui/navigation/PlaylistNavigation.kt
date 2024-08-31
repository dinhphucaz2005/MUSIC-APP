package com.example.musicapp.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.musicapp.ui.screen.playlist.PlaylistDetail
import com.example.musicapp.ui.screen.playlist.PlaylistEdit
import com.example.musicapp.ui.screen.playlist.PlaylistHome
import com.example.musicapp.ui.screen.playlist.SelectSongViewModel

@UnstableApi
fun NavGraphBuilder.playlistNavigation(navController: NavHostController) {
    navigation(startDestination = Routes.PLAYLIST_HOME.name, route = Routes.PLAYLIST.name) {
        composable(route = Routes.PLAYLIST_HOME.name) {
            PlaylistHome(navController = navController, hiltViewModel())
        }
        composable(
            route = Routes.PLAYLIST_DETAIL.name + "/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId")
            PlaylistDetail(
                navController = navController,
                viewModel = hiltViewModel(),
                playlistId
            )
        }
        composable(
            route = Routes.PLAYLIST_EDIT.name + "/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId")
            if (playlistId != null) {
                val selectSongViewModel: SelectSongViewModel = hiltViewModel()
                val name = selectSongViewModel.getPlaylistName(playlistId)
                PlaylistEdit(id = playlistId, name, navController = navController)
            } else
                navController.popBackStack()
        }
    }
}