package nd.phuc.musicapp.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import nd.phuc.musicapp.ui.player.MiniPlayer

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppScreen(
    bottomNavigationBar: @Composable (Modifier) -> Unit,
    content: @Composable () -> Unit,
) {
    var isPlayerExpanded by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(onExpand = { isPlayerExpanded = true })
                bottomNavigationBar(Modifier)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            content()
        }

        if (isPlayerExpanded) {
            FullPlayer(onClose = { isPlayerExpanded = false })
        }
    }
}

@Composable
fun FullPlayer(onClose: () -> Unit) {
    nd.phuc.musicapp.ui.player.FullPlayer(onCollapse = onClose)
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
