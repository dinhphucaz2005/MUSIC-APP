package nd.phuc.musicapp.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import nd.phuc.musicapp.ui.songs.SongsScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.automirrored.filled.List
import nd.phuc.musicapp.ui.player.FullPlayer
import nd.phuc.musicapp.ui.player.MiniPlayer

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isPlayerExpanded by remember { mutableStateOf(false) }
    val items = listOf("Songs", "Artists", "Favorites", "Playlists")
    val icons = listOf(
        Icons.Default.MusicNote,
        Icons.Default.Person,
        Icons.Default.Favorite,
        Icons.AutoMirrored.Filled.List
    )

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(onExpand = { isPlayerExpanded = true })
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedIndex) {
                0 -> SongsScreen()
                1 -> ArtistScreen()
                2 -> FavoritesScreen()
                3 -> PlaylistScreen()
                else -> Text("Screen $selectedIndex not implemented yet")
            }
        }

        if (isPlayerExpanded) {
            FullPlayer(onCollapse = { isPlayerExpanded = false })
        }
    }
}
