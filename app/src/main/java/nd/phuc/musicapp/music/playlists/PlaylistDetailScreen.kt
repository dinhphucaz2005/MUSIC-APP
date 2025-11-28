package nd.phuc.musicapp.music.playlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nd.phuc.core.presentation.components.SongItemContent
import nd.phuc.musicapp.LocalMediaControllerManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: String,
    onBackClick: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    playlistsViewModel: PlaylistsViewModel
) {
    val mediaControllerManager = LocalMediaControllerManager.current
    // In a real app, we would fetch the playlist by ID. 
    // For now, we'll just use the liked playlist as a demo if the ID matches, or empty.
    // Assuming we are just showing the "Liked" playlist for this demo as it's the only one available in the viewmodel.
    val likedPlaylist by playlistsViewModel.likedPlaylist.collectAsState()
    
    val songs = if (playlistId == "favorites") likedPlaylist.songs else emptyList()
    val title = if (playlistId == "favorites") "Favorite Songs" else "Playlist"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    songs,
                    key = { _, item -> item.id }
                ) { index, song ->
                    SongItemContent(
                        modifier = Modifier.fillMaxWidth(), song = song,
                        onSongClick = {
                            mediaControllerManager.playPlaylist(likedPlaylist, index)
                            onNavigateToPlayer()
                        },
                        onMoreChoice = {}
                    )
                }
            }
        }
    }
}
