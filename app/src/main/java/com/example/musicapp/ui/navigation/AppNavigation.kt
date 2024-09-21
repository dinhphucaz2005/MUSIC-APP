@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("DEPRECATION")

package com.example.musicapp.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.ui.Main
import com.example.musicapp.ui.MainViewModel
import com.example.musicapp.ui.screen.song.SongScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()

    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.OTHER.name
    ) {
        composable(Routes.OTHER.name) {
            Main(mainViewModel) { navController.navigate(Routes.SONG.name) }
        }
        composable(
            route = Routes.SONG.name,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = EaseInOut
                    )
                ) + scaleIn(
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = EaseInOut
                    )
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = EaseInOut
                    )
                ) + scaleOut(
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = EaseInOut
                    )
                )
            }
        ) {
            SongScreen(navHostController = navController, viewModel = mainViewModel)
        }
    }
}