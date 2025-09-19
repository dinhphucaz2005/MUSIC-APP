package nd.phuc.musicapp.music.presentation.ui.feature.playlists

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import nd.phuc.musicapp.music.presentation.ui.feature.home.components.YourPlaylistsSection
import nd.phuc.musicapp.music.presentation.ui.feature.playlists.components.PlaylistAddBottomSheet
import nd.phuc.musicapp.music.presentation.ui.feature.playlists.components.SongPickerBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel = hiltViewModel(),
) {
    val playlists by viewModel.playlists.collectAsState()
    val selectedPlaylistId by viewModel.selectedPlaylistId.collectAsState()
    val playlistSongs by viewModel.playlistSongs.collectAsState()
    val allSongs by viewModel.allSongs.collectAsState(initial = emptyList())
    var showAddSheet by remember { mutableStateOf(false) }
    var showSongPicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (selectedPlaylistId == null) {
        YourPlaylistsSection(
            playlists = playlists,
            onPlaylistClick = { playlistId ->
                viewModel.selectPlaylist(playlistId)
            }
        )
        FloatingActionButton(
            onClick = { showAddSheet = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Playlist")
        }
        if (showAddSheet) {
            ModalBottomSheet(onDismissRequest = { showAddSheet = false }) {
                PlaylistAddBottomSheet(
                    onAdd = { name ->
                        coroutineScope.launch {
                            viewModel.addPlaylist(name)
                            showAddSheet = false
                        }
                    },
                    onCancel = { showAddSheet = false }
                )
            }
        }
    } else {
        val playlist = playlists.find { it.id == selectedPlaylistId }
        PlaylistDetailScreen(
            playlistName = playlist?.name ?: "",
            songs = playlistSongs,
            onAddSong = { showSongPicker = true },
            onBack = { viewModel.selectPlaylist(playlist?.id) }
        )
        if (showSongPicker) {
            val songsNotInPlaylist =
                allSongs.filter { song -> playlistSongs.none { it.filePath == song.filePath } }
            ModalBottomSheet(onDismissRequest = { showSongPicker = false }) {
                SongPickerBottomSheet(
                    allSongs = songsNotInPlaylist,
                    onSongSelected = { song ->
                        viewModel.addSongToPlaylist(song)
                        showSongPicker = false
                    },
                    onCancel = { showSongPicker = false }
                )
            }
        }
    }
}
