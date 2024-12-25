package com.example.musicapp

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.constants.NavigationBarHeight
import com.example.musicapp.constants.Screens
import com.example.musicapp.core.presentation.components.BottomSheetMenu
import com.example.musicapp.core.presentation.components.NavigationBarAnimationSpec
import com.example.musicapp.core.presentation.components.rememberBottomSheetState
import com.example.musicapp.core.presentation.theme.Black
import com.example.musicapp.core.presentation.theme.DarkGray
import com.example.musicapp.core.presentation.theme.LightGray
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.core.presentation.theme.White
import com.example.musicapp.other.presentation.ui.screen.cloud.CloudScreen
import com.example.musicapp.other.presentation.ui.screen.home.HomeScreen
import com.example.musicapp.other.presentation.ui.screen.playlist.playlistNavigation
import com.example.musicapp.other.presentation.ui.screen.setting.LoginScreen
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import com.example.musicapp.service.MusicService
import com.example.musicapp.song.BottomSheetPlayer
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.youtube.presentation.YoutubeViewModel
import com.example.musicapp.youtube.presentation.youtubeNavigation
import dagger.hilt.android.AndroidEntryPoint


@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var mediaControllerManager by mutableStateOf<MediaControllerManager?>(null)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            mediaControllerManager =
                MediaControllerManager(this@MainActivity, binder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaControllerManager?.dispose()
            mediaControllerManager = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMusicService()
//        handlePermissions()
        setContent {
            MusicTheme {
                CompositionLocalProvider(
                    LocalMediaControllerManager provides mediaControllerManager,
                ) {
                    App(hiltViewModel<HomeViewModel>(), hiltViewModel<YoutubeViewModel>())
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        startService(Intent(this, MusicService::class.java))
        bindService(
            Intent(this, MusicService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaControllerManager = null
    }


    private fun startMusicService() {
        val musicServiceIntent = Intent(this@MainActivity, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        startService(musicServiceIntent)
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
private fun App(
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

        LaunchedEffect(navBackStackEntry) {
            playerBottomSheetState.collapseSoft()
        }

        LaunchedEffect(queue) {
            if (queue != null && playerBottomSheetState.isDismissed) {
                playerBottomSheetState.collapseSoft()
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

            youtubeNavigation(navController, youtubeViewModel)

            composable(Screens.Setting.route) {
                LoginScreen(navController = navController, reload = youtubeViewModel::reload)
            }

        }

        BottomSheetPlayer(
            state = playerBottomSheetState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .wrapContentHeight(),
            navController = navController,
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


        val menuState = LocalMenuState.current
        BottomSheetMenu(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            state = menuState,
            background = DarkGray,
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.MainNavigationBar(
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
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                )
            }
        }
    }

}