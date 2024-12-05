@file:OptIn(DelicateCoroutinesApi::class)

package com.example.musicapp

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.constants.NavigationBarHeight
import com.example.musicapp.other.presentation.ui.navigation.playlistNavigation
import com.example.musicapp.other.presentation.ui.screen.cloud.CloudScreen
import com.example.musicapp.other.presentation.ui.screen.home.HomeScreen
import com.example.musicapp.other.presentation.ui.screen.setting.LoginScreen
import com.example.musicapp.other.presentation.ui.theme.Black
import com.example.musicapp.other.presentation.ui.theme.DarkGray
import com.example.musicapp.other.presentation.ui.theme.LightGray
import com.example.musicapp.other.presentation.ui.theme.MusicTheme
import com.example.musicapp.other.presentation.ui.theme.White
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import com.example.musicapp.song.presetation.components.NavigationBarAnimationSpec
import com.example.musicapp.song.presetation.components.Screens
import com.example.musicapp.song.presetation.components.rememberBottomSheetState
import com.example.musicapp.song.presetation.song.BottomSheetPlayer
import com.example.musicapp.youtube.presentation.home.YoutubeScreen
import com.example.musicapp.youtube.presentation.home.YoutubeViewModel
import com.example.musicapp.youtube.presentation.search.YoutubeSearchScreen
import com.example.musicapp.youtube.presentation.search.YoutubeSearchViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun App(
    homeViewModel: HomeViewModel,
    youtubeViewModel: YoutubeViewModel
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val density = LocalDensity.current

    val windowsInsets = WindowInsets.systemBars
    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }
    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val navigationItems = remember { Screens.MainScreens }

    val queue by mediaControllerManager.queue.collectAsState()

    val active by rememberSaveable {
        mutableStateOf(false)
    }

    val shouldShowNavigationBar = remember(navBackStackEntry, active) { true }

    val navigationBarHeight by animateDpAsState(
        targetValue = if (shouldShowNavigationBar) NavigationBarHeight else 0.dp,
        animationSpec = NavigationBarAnimationSpec,
        label = ""
    )


    val playListViewModel = hiltViewModel<PlaylistViewModel>()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ) {
            val playerBottomSheetState = rememberBottomSheetState(
                dismissedBound = 0.dp,
                collapsedBound = bottomInset + (if (shouldShowNavigationBar) NavigationBarHeight else 0.dp) + MiniPlayerHeight,
                expandedBound = maxHeight,
            )

            LaunchedEffect(queue) {
                if (queue != null) {
                    playerBottomSheetState.collapseSoft()
                } else {
                    playerBottomSheetState.dismiss()
                }
            }

            NavHost(
                navController = navController,
                startDestination = Screens.Home.route,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = (if (shouldShowNavigationBar) NavigationBarHeight else 0.dp) + if (playerBottomSheetState.isDismissed) 0.dp else MiniPlayerHeight
                    )
                    .fillMaxSize()
            ) {

                composable(Screens.Home.route) { HomeScreen(homeViewModel) }

                playlistNavigation(navController, playListViewModel, homeViewModel)

                composable(Screens.Cloud.route) { CloudScreen() }

                navigation(
                    startDestination = Screens.YoutubeHome.route,
                    route = Screens.Youtube.route
                ) {
                    composable(Screens.YoutubeHome.route) {
                        YoutubeScreen(
                            navController = navController, youtubeViewModel = youtubeViewModel
                        )
                    }
                    composable(Screens.YoutubeSearch.route) {
                        YoutubeSearchScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = hiltViewModel<YoutubeSearchViewModel>(),
                            youtubeViewModel = youtubeViewModel
                        )
                    }
                }

                composable(Screens.Setting.route) {
                    LoginScreen(navController)
                }

            }

            BottomSheetPlayer(
                state = playerBottomSheetState,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight()
            )

            MainNavigationBar(
                modifier = Modifier
                    .offset {
                        if (navigationBarHeight == 0.dp) {
                            IntOffset(
                                x = 0, y = (bottomInset + NavigationBarHeight).roundToPx()
                            )
                        } else {
                            val slideOffset =
                                (bottomInset + NavigationBarHeight) * playerBottomSheetState.progress.coerceIn(
                                    0f, 1f
                                )
                            val hideOffset =
                                (bottomInset + NavigationBarHeight) * (1 - navigationBarHeight / NavigationBarHeight)
                            IntOffset(
                                x = 0, y = (slideOffset + hideOffset).roundToPx()
                            )
                        }
                    },
                navigationItems = navigationItems,
                navBackStackEntry = navBackStackEntry,
                navController = navController
            )
        }
}

@Preview
@Composable
private fun MainNavigationBarPreview() {
    MusicTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            MainNavigationBar(
                modifier = Modifier.fillMaxWidth(),
                navigationItems = Screens.MainScreens,
                navController = rememberNavController()
            )
        }

    }
}

@Composable
fun BoxWithConstraintsScope.MainNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<Screens>,
    navBackStackEntry: NavBackStackEntry? = null,
    navController: NavHostController
) {
    Row(
        modifier = modifier
            .background(DarkGray)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(NavigationBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationItems.fastForEach { screen ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = {
                        if (navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true) {
                            navBackStackEntry.savedStateHandle["scrollToTop"] = true
                        } else {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val color = if (navBackStackEntry
                        ?.destination
                        ?.hierarchy
                        ?.any { it.route == screen.route } == true
                ) White else LightGray

                Icon(
                    painter = painterResource(screen.iconId),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null,
                    tint = color,
                )
                Text(
                    text = stringResource(screen.titleId), color = color,
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                )
            }
        }
    }

}