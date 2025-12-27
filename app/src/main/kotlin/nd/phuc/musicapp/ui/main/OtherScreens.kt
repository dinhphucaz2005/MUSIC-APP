@file:OptIn(ExperimentalMaterial3Api::class)
package nd.phuc.musicapp.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ArtistScreen() {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Artists") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No artists found", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun FavoritesScreen() {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Favorites") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No favorites yet", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun PlaylistScreen() {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Playlists") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No playlists created", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun PlaylistDetailScreen(playlistId: String) {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Playlist Detail") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Playlist ID: $playlistId", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
