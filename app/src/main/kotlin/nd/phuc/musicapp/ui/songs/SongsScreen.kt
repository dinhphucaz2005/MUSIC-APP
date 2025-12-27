package nd.phuc.musicapp.ui.songs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.data.repository.SongRepositoryState
import nd.phuc.musicapp.model.LocalSong
import nd.phuc.musicapp.model.ThumbnailSource
import org.koin.androidx.compose.koinViewModel

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun SongsScreen(
    songsViewModel: SongsViewModel = koinViewModel(),
) {
    val mediaControllerManager = LocalMediaControllerManager.current
    val context = LocalContext.current
    val state by songsViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        songsViewModel.loadSongs(context)
    }

    SongsContent(
        state = state,
        onSongClick = { mediaControllerManager.play(it) }
    )
}

@Composable
private fun SongsContent(
    state: SongRepositoryState,
    onSongClick: (LocalSong) -> Unit,
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
                Text(
                    text = "Your Library",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${state.songs.size} songs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (state.isLoading && state.songs.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp) // Space for mini player
                ) {
                    items(state.songs) { song ->
                        SongItem(
                            song = song,
                            onClick = { onSongClick(song) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SongItem(song: LocalSong, onClick: () -> Unit) {
    ListItem(
        headlineContent = { 
            Text(
                text = song.title,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            ) 
        },
        supportingContent = { 
            Text(
                text = song.artist,
                maxLines = 1,
                color = MaterialTheme.colorScheme.secondary
            ) 
        },
        leadingContent = {
            AsyncImage(
                model = when (val source = song.thumbnailSource) {
                    is ThumbnailSource.FromUrl -> source.url
                    is ThumbnailSource.FromByteArray -> source.byteArray
                    is ThumbnailSource.FilePath -> source.path
                    else -> null
                },
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
private fun SongsScreenPreview() {
    val mockSongs = listOf(
        LocalSong("Song 1", "Artist 1", "path1", ThumbnailSource.None, 300000, false),
        LocalSong("Song 2", "Artist 2", "path2", ThumbnailSource.None, 240000, true),
        LocalSong("Song 3", "Artist 3", "path3", ThumbnailSource.None, 180000, false),
    )
    SongsContent(
        state = SongRepositoryState(songs = mockSongs, isLoading = false),
        onSongClick = {}
    )
}
