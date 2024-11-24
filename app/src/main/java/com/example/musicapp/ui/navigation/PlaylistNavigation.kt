package com.example.musicapp.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.musicapp.ui.navigation.RouteConstants.PLAYLIST_ID
import com.example.musicapp.ui.screen.playlist.PlayListDetail
import com.example.musicapp.ui.screen.playlist.PlayListEdit
import com.example.musicapp.ui.screen.playlist.PlayListHome
import com.example.musicapp.viewmodels.HomeViewModel
import com.example.musicapp.viewmodels.PlayListViewModel

fun NavGraphBuilder.playlistNavigation(
    navController: NavHostController,
    viewModel: PlayListViewModel,
    homeViewModel: HomeViewModel,
    setNavigationBarVisible: (Boolean) -> Unit,
) {
    navigation(startDestination = Routes.PLAYLIST_HOME.name, route = Routes.PLAYLIST.name) {
        composable(route = Routes.PLAYLIST_HOME.name) {
            setNavigationBarVisible(true)
            PlayListHome(navController, viewModel)
        }
        composable(
            route = Routes.PLAYLIST_DETAIL.name + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getString(PLAYLIST_ID)
            setNavigationBarVisible(true)
            PlayListDetail(playlistId, navController, viewModel)
        }
        composable(
            route = Routes.PLAYLIST_EDIT.name + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId = navBackStackEntry.arguments?.getString(PLAYLIST_ID)
            setNavigationBarVisible(true)
            PlayListEdit(playlistId ?: "", navController, viewModel, homeViewModel)
        }
    }
}