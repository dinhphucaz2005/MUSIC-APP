package nd.phuc.musicapp.music.presentation.ui.feature.playlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nd.phuc.core.presentation.components.SongItemContent
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.di.fakeViewModel

@Preview
@Composable
private fun PlaylistScreenPreview() {
    PlaylistScreen(
        playlistsViewModel = fakeViewModel()
    )
}

@Composable
fun PlaylistScreen(
    playlistsViewModel: PlaylistsViewModel,
) {
    val mediaControllerManager = LocalMediaControllerManager.current
    val likedPlaylist by playlistsViewModel.likedPlaylist.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF7F7F7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Favorite Songs (${likedPlaylist.songs.size})",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    likedPlaylist.songs,
                    key = { index, item -> item.id }
                ) { index, song ->
                    SongItemContent(
                        modifier = Modifier.fillMaxWidth(), song = song,
                        onSongClick = {
                            mediaControllerManager.playPlaylist(likedPlaylist, index)
                        },
                        onMoreChoice = {}
                    )
                }
            }
        }
    }
}
