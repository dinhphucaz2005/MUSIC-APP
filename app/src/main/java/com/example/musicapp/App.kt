@file:OptIn(DelicateCoroutinesApi::class)

package com.example.musicapp

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.core.presentation.components.NavigationBar
import com.example.musicapp.other.presentation.ui.navigation.Routes
import com.example.musicapp.other.presentation.ui.navigation.playlistNavigation
import com.example.musicapp.other.presentation.ui.screen.cloud.CloudScreen
import com.example.musicapp.other.presentation.ui.screen.home.HomeScreen
import com.example.musicapp.other.presentation.ui.screen.setting.LoginScreen
import com.example.musicapp.other.presentation.ui.theme.MusicTheme
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlayListViewModel
import com.example.musicapp.song.presetation.components.rememberBottomSheetState
import com.example.musicapp.song.presetation.song.BottomSheetPlayer
import com.example.musicapp.youtube.presentation.home.YoutubeScreen
import com.example.musicapp.youtube.presentation.home.YoutubeViewModel
import com.example.musicapp.youtube.presentation.search.YoutubeSearchScreen
import com.example.musicapp.youtube.presentation.search.YoutubeSearchViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

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
        visible = isVisible, enter = fadeIn() + expandVertically(
            animationSpec = tween(
                durationMillis = 100, easing = LinearEasing
            )
        ), exit = fadeOut() + shrinkVertically()
    ) {
        Column {
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

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun App(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    var isNavigationBarVisible by rememberSaveable { mutableStateOf(true) }

    val density = LocalDensity.current
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val youtubeViewModel = hiltViewModel<YoutubeViewModel>()

    val windowsInsets = WindowInsets.systemBars
    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }


    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        bottomBar = { CustomBottomBar(isNavigationBarVisible, navController) }
    ) { paddingValues ->
        val playListViewModel = hiltViewModel<PlayListViewModel>()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val playerBottomSheetState = rememberBottomSheetState(
                dismissedBound = 0.dp,
                collapsedBound = bottomInset + MiniPlayerHeight,
                expandedBound = maxHeight,
            )
            LaunchedEffect(Unit) {
                playerBottomSheetState.expandSoft()
            }
            LaunchedEffect(playerBottomSheetState) {
                if (playerBottomSheetState.isCollapsed)
                    isNavigationBarVisible = true
                if (playerBottomSheetState.isExpanded)
                    isNavigationBarVisible = false
            }

            NavHost(
                navController = navController,
                startDestination = Routes.HOME.name,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(Routes.HOME.name) {
                    isNavigationBarVisible = true
                    HomeScreen(homeViewModel = homeViewModel)
                }

                playlistNavigation(navController, playListViewModel, homeViewModel) {
                    isNavigationBarVisible = it
                }

                composable(Routes.CLOUD.name) {
                    isNavigationBarVisible = true
                    CloudScreen()
                }

                navigation(
                    startDestination = Routes.YOUTUBE_HOME.name,
                    route = Routes.YOUTUBE.name
                ) {
                    composable(Routes.YOUTUBE_HOME.name) {
                        isNavigationBarVisible = true
                        YoutubeScreen(navController = navController, viewModel = youtubeViewModel)
                    }
                    composable(Routes.YOUTUBE_SEARCH.name) {
                        isNavigationBarVisible = true
                        YoutubeSearchScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = hiltViewModel<YoutubeSearchViewModel>(),
                            youtubeViewModel = youtubeViewModel
                        )
                    }
                }


                composable(Routes.SETTING.name) {
                    isNavigationBarVisible = true
                    LoginScreen(navController)
                }

                composable(Routes.SONG.name) {
                    isNavigationBarVisible = false
                }
            }
            BottomSheetPlayer(
                state = playerBottomSheetState,
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}