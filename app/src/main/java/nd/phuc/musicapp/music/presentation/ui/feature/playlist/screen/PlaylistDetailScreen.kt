/*
package nd.phuc.musicapp.music.presentation.ui.feature.playlist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.LocalMenuState
import nd.phuc.core.presentation.previews.ExtendDevicePreviews
import nd.phuc.musicapp.di.fakeViewModel
import nd.phuc.musicapp.music.presentation.ui.feature.playlist.PlaylistDetailViewModel
import nd.phuc.musicapp.music.presentation.ui.feature.playlist.components.PlaylistHeader
import nd.phuc.core.presentation.theme.MyMusicAppTheme

@ExtendDevicePreviews
@Composable
private fun PlaylistDetailScreenPreview() {
    MyMusicAppTheme {
        PlaylistDetailScreen(
            viewModel = fakeViewModel(),
            onNavigateBack = {}
        )
    }
}

@Composable
fun PlaylistDetailScreen(
    viewModel: PlaylistDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val playlistState by viewModel.playlistState.collectAsState()
    val mediaControllerManager = LocalMediaControllerManager.current
    val menuState = LocalMenuState.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = playlistState) {
            is PlaylistDetailViewModel.PlaylistState.Idle,
            is PlaylistDetailViewModel.PlaylistState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is PlaylistDetailViewModel.PlaylistState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        PlaylistHeader(
                            playlistName = state.playlist.name,
                            songCount = state.songs.size,
                            onBackClick = onNavigateBack,
                            onPlayClick = {
                                if (state.songs.isNotEmpty()) {
                                    mediaControllerManager.playQueue(
                                        songs = state.songs,
                                        index = 0,
                                        id = state.playlist.id.toString()
                                    )
                                }
                            },
                            playlistCoverUrl = "TODO()",
                            totalDuration = "55:50",
                            onShuffleClick = {}
                        )
                    }

                    items(state.songs.size) { index ->
                        val song = state.songs[index]
//                        SongItemContent(
//                            song = song,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp, vertical = 4.dp),
//                            onSongClick = {
//                                mediaControllerManager.playQueue(
//                                    songs = state.songs,
//                                    index = index,
//                                    id = state.playlist.id.toString()
//                                )
//                            },
//                            onMoreChoice = {
//                                menuState.show {
//                                    Box(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .height(300.dp)
//                                            .background(color = MaterialTheme.colorScheme.primary)
//                                    )
//                                }
//                            }
//                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // Bottom spacing for player
                    }
                }
            }

            is PlaylistDetailViewModel.PlaylistState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Error: ${state.message}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}*/
