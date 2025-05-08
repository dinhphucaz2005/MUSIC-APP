package com.example.musicapp.other.presentation.ui.screen.playlist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapp.extension.launchIO
import com.example.musicapp.music.domain.model.Song
import com.example.musicapp.music.data.database.entity.SongEntity
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPlaylistScreen(
    playlistViewModel: PlaylistViewModel,
    dismiss: () -> Unit,
    songs: List<Song>,
    homeViewModel: HomeViewModel
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val scope = CoroutineScope(Dispatchers.IO)

    val onSavePlaylist = {
        val songEntities = songs.map {
            SongEntity(it.id, it.title, it.artist, it.uri, it.duration)
        }
        scope.launchIO {
            playlistViewModel.savePlaylist(name, description, "user", songEntities)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Playlist Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onSavePlaylist()
                dismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Playlist")
        }
    }
}