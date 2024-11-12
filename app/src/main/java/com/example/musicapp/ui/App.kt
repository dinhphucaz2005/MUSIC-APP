package com.example.musicapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.R
import com.example.musicapp.ui.components.MiniPlayer
import com.example.musicapp.ui.components.NavigationBar
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.navigation.playlistNavigation
import com.example.musicapp.ui.screen.cloud.CloudScreen
import com.example.musicapp.ui.screen.home.HomeScreen
import com.example.musicapp.ui.screen.setting.SettingScreen
import com.example.musicapp.ui.screen.song.SongScreen
import com.example.musicapp.ui.screen.youtube.YoutubeScreen
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.viewmodels.PlayListViewModel
import com.example.musicapp.viewmodels.SongViewModel

data class Item(
    val painter: Painter, val route: String
)


@Preview(showBackground = true)
@Composable
fun Preview() {
    MusicTheme {
        App()
    }
}

@Composable
fun CustomBottomBar(isVisible: Boolean, navController: NavHostController) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column {
            MiniPlayer(viewModel = hiltViewModel<SongViewModel>(),
                navigateToSongScreen = { navController.navigate(Routes.SONG.name) })
            NavigationBar(
                navController = navController, list = listOf(
                    Item(painterResource(id = R.drawable.ic_home), Routes.HOME.name),
                    Item(painterResource(id = R.drawable.ic_disc), Routes.PLAYLIST.name),
                    Item(painterResource(id = R.drawable.ic_cloud), Routes.CLOUD.name),
                    Item(painterResource(id = R.drawable.ic_youtube), Routes.YOUTUBE.name),
                    Item(painterResource(id = R.drawable.ic_setting), Routes.SETTING.name),
                )
            )
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    var isNavigationBarVisible by rememberSaveable { mutableStateOf(true) }

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        bottomBar = { CustomBottomBar(isNavigationBarVisible, navController) }) { paddingValues ->

        val playListViewModel = hiltViewModel<PlayListViewModel>()
        NavHost(
            navController = navController,
            startDestination = Routes.HOME.name,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Routes.HOME.name) {
                isNavigationBarVisible = true
                HomeScreen()
            }

            playlistNavigation(navController, playListViewModel) {
                isNavigationBarVisible = it
            }

            composable(Routes.CLOUD.name) {
                isNavigationBarVisible = true
                CloudScreen()
            }

            composable(Routes.YOUTUBE.name) {
                isNavigationBarVisible = true
                YoutubeScreen()
            }

            composable(Routes.SETTING.name) {
                isNavigationBarVisible = true
                SettingScreen()
            }

            composable(Routes.SONG.name) {
                isNavigationBarVisible = false
                SongScreen(
                    navController = navController, viewModel = hiltViewModel<SongViewModel>()
                )
            }
        }
    }
}