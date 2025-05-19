package com.example.musicapp

import android.annotation.SuppressLint
import android.content.ComponentName
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.audio_spectrum.AudioVisualizerScreen
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.constants.NavigationBarHeight
import com.example.musicapp.constants.Screens
import com.example.musicapp.core.presentation.components.BottomSheetMenu
import com.example.musicapp.core.presentation.components.NavigationBarAnimationSpec
import com.example.musicapp.core.presentation.components.rememberBottomSheetState
import com.example.musicapp.music.domain.repository.SongRepository
import com.example.musicapp.music.presentation.ui.screen.home.HomeScreen
import com.example.musicapp.music.presentation.ui.screen.home.HomeViewModel
import com.example.musicapp.service.MusicService
import com.example.musicapp.core.presentation.components.BottomSheetPlayer
import com.example.musicapp.music.presentation.navigation.Routes
import com.example.musicapp.other.presentation.ui.screen.playlist.playlistNavigation
import com.example.musicapp.other.presentation.ui.screen.playlist.PlaylistViewModel
import com.example.musicapp.ui.theme.darkGray
import com.example.musicapp.ui.theme.lightGray
import com.example.musicapp.ui.theme.MyMusicAppTheme
import com.example.musicapp.ui.theme.white
import com.example.musicapp.util.MediaControllerManagerImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var songRepository: SongRepository

    private var mediaControllerManager by mutableStateOf<MediaControllerManagerImpl?>(null)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            mediaControllerManager =
                MediaControllerManagerImpl(this@MainActivity, binder, songRepository)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaControllerManager?.dispose()
            mediaControllerManager = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMusicService()
        setContent {
            MyMusicAppTheme {
                CompositionLocalProvider(
                    LocalMediaControllerManager provides mediaControllerManager,
                ) {
                    App()
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
            BIND_AUTO_CREATE
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
private fun App() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()


    val density = LocalDensity.current

    val windowsInsets = WindowInsets.systemBars
    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }
    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val navigationItems = remember { Screens.MainScreens }

    val queue by mediaControllerManager.queue.collectAsState()

    val active by rememberSaveable { mutableStateOf(false) }

    val shouldShowNavigationBar = remember(navBackStackEntry, active) { true }

    val navigationBarHeight by animateDpAsState(
        targetValue = if (shouldShowNavigationBar) NavigationBarHeight else 0.dp,
        animationSpec = NavigationBarAnimationSpec,
        label = ""
    )

    val playlistViewModel = hiltViewModel<PlaylistViewModel>()

    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {
        composable(route = Routes.HOME_SCREEN) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
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

                val horizontalPagerState = rememberPagerState(pageCount = navigationItems::size)

                HorizontalPager(
                    state = horizontalPagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = (if (shouldShowNavigationBar) NavigationBarHeight else 0.dp) + if (playerBottomSheetState.isDismissed) 0.dp else MiniPlayerHeight
                        )
                        .fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> HomeScreen(
                            homeViewModel = hiltViewModel<HomeViewModel>(),
                            onNavigateToAllLocalSongScreen = { navController.navigate(Routes.LOCAL_SONGS) }
                        )

                        1 -> Column {
                            Text(
                                "Playlist",
                                style = MaterialTheme.typography.titleLarge.copy(color = white)
                            )
                        }
//                1 -> playlistNavigation(navController, playListViewModel, homeViewModel)
                        2 -> Column {
                            Text(
                                "Cloud",
                                style = MaterialTheme.typography.titleLarge.copy(color = white)
                            )
                        }

                        3 -> Column {
                            Text(
                                "Youtube",
                                style = MaterialTheme.typography.titleLarge.copy(color = white)
                            )
                        }

                        4 -> Column {
                            Text(
                                "Login",
                                style = MaterialTheme.typography.titleLarge.copy(color = white)
                            )
                        }

                        5 -> AudioVisualizerScreen()
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
                    //            navController = navController
                    state = horizontalPagerState
                )


                val menuState = LocalMenuState.current
                BottomSheetMenu(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    state = menuState,
                    background = darkGray,
                )
            }
        }

        playlistNavigation(
            navController = navController,
            playlistViewModel = playlistViewModel,
        )
    }

}

@Composable
private fun BoxWithConstraintsScope.MainNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<Screens>,
    state: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .background(darkGray)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(NavigationBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationItems.fastForEachIndexed { index, screen ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = {
                        coroutineScope.launch {
                            state.animateScrollToPage(index)
                        }
                    }),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val color = if (state.currentPage == index) white else lightGray

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