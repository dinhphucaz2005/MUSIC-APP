package nd.phuc.musicapp.music.presentation.ui.feature.playlists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nd.phuc.core.domain.model.LocalSong

@Composable
fun PlaylistDetailScreen(
    playlistName: String,
    songs: List<LocalSong>,
    onAddSong: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = playlistName, modifier = Modifier.padding(bottom = 8.dp))
        Button(onClick = onAddSong, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("Add Song")
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Text("Back")
        }
        Text(text = "Songs in Playlist:", modifier = Modifier.padding(bottom = 8.dp))
        songs.forEach { song ->
            Text(text = song.title, modifier = Modifier.padding(bottom = 4.dp))
        }
    }
}

