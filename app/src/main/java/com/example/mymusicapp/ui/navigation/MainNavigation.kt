package com.example.mymusicapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymusicapp.ui.screen.home.HomeScreen
import com.example.mymusicapp.ui.screen.playlist.PlaylistScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
    navController: NavHostController,
    homeNavController: NavHostController,
    contentPadding: PaddingValues
) {
    NavHost(navController = homeNavController, startDestination = OtherRoutes.HOME.name) {
        composable(OtherRoutes.HOME.name) {
            HomeScreen(modifier = Modifier.padding(contentPadding), navController = navController)
        }
        composable(OtherRoutes.PLAYLIST.name) {
            PlaylistScreen()
        }
    }
}