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
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }

        if (isPlayerExpanded) {
            FullPlayer(onClose = { isPlayerExpanded = false })
        }
    }
}

@Composable
fun FullPlayer(onClose: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Full Player", style = MaterialTheme.typography.titleLarge)
                Button(onClick = onClose) { Text("Close") }
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

    NavigationBar(modifier = modifier) {
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
                        }
                    )
                },
                selected = isSelected,
                onClick = {
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
    }
}
