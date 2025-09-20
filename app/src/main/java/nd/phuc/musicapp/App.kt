/*
package nd.phuc.musicapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nd.phuc.core.domain.model.MiniPlayerHeight
import nd.phuc.core.domain.model.NavigationBarHeight
import nd.phuc.core.extension.appHiltViewModel
import nd.phuc.core.extension.routeComposable
import nd.phuc.core.presentation.components.rememberBottomSheetState
import nd.phuc.musicapp.music.BottomSheetPlayer
import nd.phuc.musicapp.music.presentation.ui.feature.home.HomeViewModel
import nd.phuc.musicapp.music.presentation.ui.feature.home.screen.HomeScreen
import nd.phuc.musicapp.music.presentation.ui.feature.library.LibraryScreen
import nd.phuc.musicapp.music.presentation.ui.feature.playlists.PlaylistScreen
import nd.phuc.musicapp.music.presentation.ui.feature.playlists.PlaylistsViewModel
import timber.log.Timber
import kotlin.math.absoluteValue

@SuppressLint("UnsafeOptInUsageError")
@Deprecated(
    "No longer used, use AppScreen instead",
)
@Composable
fun App() {
    val density = LocalDensity.current
    val windowsInsets = WindowInsets.systemBars
    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowMiniPlayer = remember(currentRoute) {
        when (currentRoute) {
            Screens.Home.route,
            Screens.Playlists.route,
                -> true

            else -> false
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val maxHeight = this.maxHeight

        val playerBottomSheetState = rememberBottomSheetState(
            dismissedBound = 0.dp,
            collapsedBound = bottomInset + (if (shouldShowMiniPlayer) NavigationBarHeight else 0.dp) + MiniPlayerHeight,
            expandedBound = maxHeight,
        )
        LaunchedEffect(Unit) {
            playerBottomSheetState.collapseSoft()
        }
        LaunchedEffect(playerBottomSheetState.value) {
            Timber.i("dismissedBound: ${playerBottomSheetState.dismissedBound}")
            Timber.i("collapsedBound: ${playerBottomSheetState.collapsedBound}")
            Timber.i("expandedBound: ${playerBottomSheetState.expandedBound}")
            Timber.i("value: ${playerBottomSheetState.value}")
            Timber.i("isDismissed: ${playerBottomSheetState.isDismissed}")
            Timber.i("isCollapsed: ${playerBottomSheetState.isCollapsed}")
            Timber.i("isExpanded: ${playerBottomSheetState.isExpanded}")
            Timber.i("progress: ${playerBottomSheetState.progress}")
            Timber.i("===============================")
        }

        val fabBottomOffset by remember {
            derivedStateOf {
                if (playerBottomSheetState.isDismissed) 0.dp
                else MiniPlayerHeight * (playerBottomSheetState.progress.absoluteValue.coerceIn(
                    0f, 1f
                ) + 1f)
            }
        }

        val fabHorizontalOffset by remember {
            derivedStateOf {
                // Dịch sang phải tối đa 80.dp khi expanded
                (80.dp * playerBottomSheetState.progress.coerceIn(0f, 1f))
            }
        }

        val fabAlpha by remember {
            derivedStateOf {
                1f - playerBottomSheetState.progress.coerceIn(0f, 1f)
            }
        }




        ConstraintLayout {
            val (navigationBarRef, fabRef, miniPlayerRef, contentRef) = createRefs()

            NavHost(
                navController = navController,
                startDestination = Screens.Home.route,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(contentRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(navigationBarRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }
                    .background(color = MaterialTheme.colorScheme.background)) {
                routeComposable(Screens.Home) { HomeScreen(homeViewModel = appHiltViewModel<HomeViewModel>()) }
                routeComposable(Screens.Playlists) { PlaylistScreen(playlistsViewModel = appHiltViewModel<PlaylistsViewModel>()) }
                routeComposable(Screens.Library) { LibraryScreen() }
            }

            BottomSheetPlayer(
                state = playerBottomSheetState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(miniPlayerRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            AppNavigationBar(
                modifier = Modifier
                    .constrainAs(navigationBarRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .offset {
                        IntOffset(
                            x = 0,
                            y = (NavigationBarHeight * playerBottomSheetState.progress.coerceIn(
                                0f, 1f
                            )).roundToPx()
                        )
                    }, navigationItems = listOf(
                    Screens.Home,
                    Screens.Playlists,
                    Screens.Library,
                ), navController = navController
            )

            FloatingActionButton(
                onClick = { playerBottomSheetState.expandSoft() },
                modifier = Modifier
                    .padding(16.dp)
                    .offset {
                        IntOffset(
                            x = -fabHorizontalOffset.roundToPx(), y = -fabBottomOffset.roundToPx()
                        )
                    }
                    .constrainAs(fabRef) {
                        bottom.linkTo(navigationBarRef.top)
                        end.linkTo(parent.end)
                    }
                    .graphicsLayer { alpha = fabAlpha }
                    .size(48.dp)) {
                Text("Expand")
            }

        }
    }

}




*/
