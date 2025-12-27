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
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.data.repository.SongRepository
import nd.phuc.musicapp.model.LocalSong
import org.koin.androidx.compose.koinViewModel

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

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading && state.songs.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn {
                items(state.songs) { song ->
                    SongItem(
                        song = song,
                        onClick = { mediaControllerManager.play(song) }
                    )
                }
            }
        }
    }
}

@Composable
fun SongItem(song: LocalSong, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(song.title) },
        supportingContent = { Text(song.artist) },
        modifier = Modifier.clickable { onClick() }
    )
}
