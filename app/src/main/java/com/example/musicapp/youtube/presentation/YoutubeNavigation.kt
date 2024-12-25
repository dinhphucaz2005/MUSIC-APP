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
import com.example.musicapp.constants.Screens
import com.example.musicapp.youtube.presentation.YoutubeRoute.ALBUM_ID
import com.example.musicapp.youtube.presentation.YoutubeRoute.ARTIST_ID
import com.example.musicapp.youtube.presentation.YoutubeRoute.PLAYLIST_ID
import com.example.musicapp.youtube.presentation.screen.AlbumScreen
import com.example.musicapp.youtube.presentation.screen.ArtistScreen
import com.example.musicapp.youtube.presentation.screen.PlaylistScreen
import com.example.musicapp.youtube.presentation.screen.YoutubeScreen
import com.example.musicapp.youtube.presentation.search.YoutubeSearchScreen
import com.example.musicapp.youtube.presentation.search.YoutubeSearchViewModel

fun NavGraphBuilder.youtubeNavigation(
    navController: NavHostController,
    youtubeViewModel: YoutubeViewModel,
) {
    navigation(startDestination = YoutubeRoute.HOME, route = Screens.Youtube.route) {
        composable(YoutubeRoute.HOME) {
            YoutubeScreen(
                navController = navController, youtubeViewModel = youtubeViewModel
            )
        }

        composable(
            route = YoutubeRoute.PLAYLIST_DETAIL + "/{$PLAYLIST_ID}",
            arguments = listOf(navArgument(PLAYLIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val playlistId =
                navBackStackEntry.arguments?.getString(PLAYLIST_ID) ?: return@composable
            PlaylistScreen(
                playlistId = playlistId,
                navController = navController,
                youtubeViewModel = youtubeViewModel
            )
        }

        composable(YoutubeRoute.SEARCH) {
            YoutubeSearchScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = hiltViewModel<YoutubeSearchViewModel>()
            )
        }

        composable(
            YoutubeRoute.ARTIST + "/{$ARTIST_ID}",
            arguments = listOf(navArgument(ARTIST_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val artistId = navBackStackEntry.arguments?.getString(ARTIST_ID) ?: return@composable
            ArtistScreen(
                modifier = Modifier.fillMaxSize(),
                artistId = artistId,
                youtubeViewModel = youtubeViewModel,
                navController = navController
            )
        }

        composable(
            route = YoutubeRoute.ALBUM + "/{$ALBUM_ID}",
            arguments = listOf(navArgument(ALBUM_ID) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val albumId = navBackStackEntry.arguments?.getString(ALBUM_ID) ?: return@composable
            AlbumScreen(
                modifier = Modifier.fillMaxSize(),
                albumId = albumId,
                youtubeViewModel = youtubeViewModel
            )
        }

    }
}