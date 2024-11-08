package com.example.musicapp.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.R
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.constants.NavigationBarHeight
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.animation.Animator
import com.example.musicapp.ui.components.MiniPlayer
import com.example.musicapp.ui.components.NavigationBar
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.navigation.playlistNavigation
import com.example.musicapp.ui.screen.cloud.CloudScreen
import com.example.musicapp.ui.screen.home.HomeScreen
import com.example.musicapp.ui.screen.setting.SettingScreen
import com.example.musicapp.ui.screen.song.SongScreen
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Item(
    val painter: Painter, val route: String
)


@Preview(showBackground = true)
@Composable
fun Preview() {
    MusicTheme {
        MainScreen(FakeModule.provideMainViewModel())
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {

    val list = listOf(
        Item(painterResource(id = R.drawable.ic_home), Routes.HOME.name),
        Item(painterResource(id = R.drawable.ic_disc), Routes.PLAYLIST.name),
        Item(painterResource(id = R.drawable.ic_cloud), Routes.CLOUD.name),
        Item(painterResource(id = R.drawable.ic_setting), Routes.SETTING.name),
    )

    val navController = rememberNavController()
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    var isMiniPlayerExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.BottomCenter
    ) {

        NavHost(
            navController = navController,
            startDestination = list[0].route,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = MiniPlayerHeight + NavigationBarHeight)
        ) {
            composable(Routes.HOME.name) { HomeScreen(viewModel = viewModel) }
            playlistNavigation(navController)
            composable(Routes.CLOUD.name) { CloudScreen(viewModel = hiltViewModel()) }
            composable(Routes.SETTING.name) { SettingScreen() }
        }

        AnimatedContent(isMiniPlayerExpanded,
            label = "",
            modifier = Modifier.align(Alignment.BottomCenter),
            transitionSpec = {
                val fadeInSpec = Animator.enterAnimation
                val scaleInSpec = scaleIn(
                    initialScale = 0.85f, animationSpec = tween(
                        durationMillis = 400, easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)
                    )
                )
                val fadeOutSpec = Animator.exitAnimation

                (fadeInSpec + scaleInSpec).togetherWith(fadeOutSpec)
            }) { targetState ->
            if (targetState) {
                SongScreen(navController = navController, viewModel = viewModel) {
                    isMiniPlayerExpanded = false
                }
            } else {
                Column {
                    MiniPlayer(
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                isMiniPlayerExpanded = true
                            }
                        }, viewModel = viewModel
                    )
                    NavigationBar(
                        navController = navController, list = list
                    )
                }

            }
        }
    }
}