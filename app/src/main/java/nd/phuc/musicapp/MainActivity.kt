package nd.phuc.musicapp

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.MiniPlayerHeight
import nd.phuc.core.domain.model.NavigationBarHeight
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import nd.phuc.core.presentation.components.BottomSheetMenu
import nd.phuc.core.presentation.components.NavigationBarAnimationSpec
import nd.phuc.core.presentation.components.rememberBottomSheetState
import nd.phuc.core.presentation.theme.MyMusicAppTheme
import nd.phuc.musicapp.music.BottomSheetPlayer
import nd.phuc.musicapp.music.presentation.ui.feature.home.HomeViewModel
import nd.phuc.musicapp.music.presentation.ui.feature.home.screen.HomeScreen
import nd.phuc.musicapp.music.presentation.ui.feature.playlists.PlaylistsScreen
import nd.phuc.musicapp.service.MusicService
import nd.phuc.musicapp.util.MediaControllerManager
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var songRepository: LocalSongRepository

    @Inject
    lateinit var mediaControllerManager: MediaControllerManager

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            mediaControllerManager.initialize(
                context = this@MainActivity,
                binder = binder,
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaControllerManager.dispose()
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
        startService(
            Intent(
                this,
                MusicService::class.java
            )
        )
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
        mediaControllerManager.dispose()
    }


    private fun startMusicService() {
        val musicServiceIntent = Intent(
            this@MainActivity,
            MusicService::class.java
        )
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        startService(musicServiceIntent)
    }


}


@Immutable
sealed class Screens(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
) {
    data object Home : Screens(R.string.home, R.drawable.ic_home)
    data object Playlists : Screens(R.string.playlists, R.drawable.ic_disc)

    companion object {
        val MainScreens = listOf(Home, Playlists)
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun App() {
    val density = LocalDensity.current

    val windowsInsets = WindowInsets.systemBars
    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }

    val navigationItems = remember { Screens.MainScreens }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { navigationItems.size }
    )

    val active by rememberSaveable { mutableStateOf(false) }

    val shouldShowNavigationBar = remember(pagerState, active) { true }

    val navigationBarHeight by animateDpAsState(
        targetValue = if (shouldShowNavigationBar) NavigationBarHeight else 0.dp,
        animationSpec = NavigationBarAnimationSpec,
        label = ""
    )

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

        LaunchedEffect(pagerState) {
            playerBottomSheetState.collapseSoft()
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = (if (shouldShowNavigationBar) NavigationBarHeight else 0.dp) + if (playerBottomSheetState.isDismissed) 0.dp else MiniPlayerHeight
                )
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) { page ->
            val navigationItem = navigationItems[page]
            when (navigationItem) {
                Screens.Home -> HomeScreen(homeViewModel = hiltViewModel<HomeViewModel>())
                Screens.Playlists -> PlaylistsScreen()
            }
        }

        BottomSheetPlayer(
            state = playerBottomSheetState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .wrapContentHeight(),
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
            pagerState = pagerState
        )


        val menuState = LocalMenuState.current
        BottomSheetMenu(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            state = menuState,
            background = MaterialTheme.colorScheme.secondary,
        )
    }

}

@Composable
private fun BoxWithConstraintsScope.MainNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<Screens>,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(NavigationBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationItems.fastForEachIndexed { index, screen ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    index,
                                    animationSpec = tween(
                                        durationMillis = 600,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        },
                    ),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val color = MaterialTheme.colorScheme.primary
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