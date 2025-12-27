package nd.phuc.musicapp.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import nd.phuc.musicapp.ui.player.MiniPlayer
import androidx.compose.ui.text.font.FontWeight
import nd.phuc.musicapp.ui.player.FullPlayer
import timber.log.Timber

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    bottomNavigationBar: @Composable (Modifier) -> Unit,
    content: @Composable () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded, skipHiddenState = true
        )
    )
    val scope = rememberCoroutineScope()

    // Get screen height to calculate progress
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(LocalDensity.current) {
        configuration.screenHeightDp.dp.toPx()
    }
    val peekHeightPx = with(LocalDensity.current) {
        72.dp.toPx()
    }

    // Calculate expansion progress (0.0 for collapsed, 1.0 for expanded)
    val progress by remember {
        derivedStateOf {
            val offset = try {
                scaffoldState.bottomSheetState.requireOffset()
            } catch (e: Exception) {
                Timber.e(e, "Error getting bottom sheet offset: ${e.message}")
                screenHeightPx - peekHeightPx
            }
            val totalDistance = screenHeightPx - peekHeightPx
            if (totalDistance > 0) {
                (1f - (offset / totalDistance)).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Full Player (fades in as we expand)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = progress }) {
                    FullPlayer(
                        onCollapse = {
                            scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                        }
                    )
                }

                // Mini Player (fades out as we expand)
                if (progress < 0.99f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .graphicsLayer { alpha = 1f - progress }
                    ) {
                        MiniPlayer(
                            onExpand = {
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            }
                        )
                    }
                }
            }
        },
        sheetPeekHeight = 72.dp,
        sheetDragHandle = null,
        sheetSwipeEnabled = true,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        sheetTonalElevation = 8.dp,
        sheetShadowElevation = 16.dp
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
                // Navigation Bar (fades out as we expand)
                Box(modifier = Modifier.graphicsLayer {
                    alpha = 1f - (progress * 2).coerceIn(0f, 1f)
                    translationY = progress * 100f // Slide down slightly
                }) {
                    if (progress < 0.5f) {
                        bottomNavigationBar(Modifier)
                    }
                }
            }
        }
    }
}


@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<String>,
    navController: NavHostController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    AppNavigationBarContent(
        modifier = modifier,
        navigationItems = navigationItems,
        currentRoute = currentRoute,
        onNavigate = { route ->
            if (route != currentRoute) {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    )
}

@Composable
fun AppNavigationBarContent(
    modifier: Modifier = Modifier,
    navigationItems: List<String>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        navigationItems.forEach { route ->
            val isSelected = currentRoute == route || (route == "home" && currentRoute == null)

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (route) {
                            "home" -> Icons.Default.Home
                            "playlist" -> Icons.AutoMirrored.Filled.PlaylistPlay
                            "artist" -> Icons.Default.LibraryMusic
                            else -> Icons.Default.Home
                        },
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = when (route) {
                            "home" -> "Home"
                            "playlist" -> "Playlists"
                            "artist" -> "Library"
                            else -> "Unknown"
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = isSelected,
                onClick = { onNavigate(route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    AppScreen(
        bottomNavigationBar = { modifier ->
            AppNavigationBarContent(
                modifier = modifier,
                navigationItems = listOf("home", "playlist", "artist"),
                currentRoute = "home",
                onNavigate = {}
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("App Content")
            }
        }
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun AppNavigationBarPreview() {
    AppNavigationBarContent(
        navigationItems = listOf("home", "playlist", "artist"),
        currentRoute = "home",
        onNavigate = {}
    )
}
