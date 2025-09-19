package nd.phuc.musicapp.music.presentation.ui.feature.playlists.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nd.phuc.core.domain.model.LocalSong

@Composable
fun SongPickerBottomSheet(
    allSongs: List<LocalSong>,
    onSongSelected: (LocalSong) -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Pick a Song", modifier = Modifier.padding(bottom = 8.dp))
        allSongs.forEach { song ->
            Button(
                onClick = { onSongSelected(song) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
            ) {
                Text(song.title)
            }
        }
        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Cancel")
        }
    }
}

