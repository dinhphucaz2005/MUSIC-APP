@file:OptIn(ExperimentalMaterial3Api::class)

package nd.phuc.musicapp.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ArtistScreen() {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Artists", fontWeight = FontWeight.Bold) })
        }
    ) { innerPadding ->
        EmptyState(
            modifier = Modifier.padding(innerPadding),
            icon = Icons.Default.Person,
            message = "No artists found"
        )
    }
}

@Composable
fun FavoritesScreen() {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Favorites", fontWeight = FontWeight.Bold) })
        }
    ) { innerPadding ->
        EmptyState(
            modifier = Modifier.padding(innerPadding),
            icon = Icons.Default.Favorite,
            message = "No favorites yet"
        )
    }
}

@Composable
fun PlaylistScreen() {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Playlists", fontWeight = FontWeight.Bold) })
        }
    ) { innerPadding ->
        EmptyState(
            modifier = Modifier.padding(innerPadding),
            icon = Icons.AutoMirrored.Filled.List,
            message = "No playlists created"
        )
    }
}

@Composable
fun PlaylistDetailScreen(playlistId: String) {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Playlist Detail", fontWeight = FontWeight.Bold) })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Playlist ID: $playlistId", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    message: String,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistScreenPreview() {
    ArtistScreen()
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen()
}

@Preview(showBackground = true)
@Composable
fun PlaylistScreenPreview() {
    PlaylistScreen()
}

@Preview(showBackground = true)
@Composable
fun PlaylistDetailScreenPreview() {
    PlaylistDetailScreen(playlistId = "mock_playlist_id")
}
