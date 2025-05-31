package com.example.musicapp.music.presentation.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.LocalMenuState
import com.example.musicapp.core.presentation.components.SongItemContent
import com.example.musicapp.core.presentation.previews.ExtendDevicePreviews
import com.example.musicapp.di.FakeModule
import com.example.musicapp.music.presentation.ui.feature.home.HomeViewModel
import com.example.musicapp.ui.theme.MyMusicAppTheme

@ExtendDevicePreviews
@Composable
private fun YourSongsSectionPreview() {
    MyMusicAppTheme {
        Box(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
        ) {
            YourSongsSection(
                localSongState = HomeViewModel.LocalSongState.Success(
                    songs = listOf(
                        FakeModule.localSong,
                        FakeModule.localSong,
                        FakeModule.localSong,
                        FakeModule.localSong,
                        FakeModule.localSong,
                        FakeModule.localSong,
                    ),
                    lastReloadMillis = 0
                ),
                onSeeAllClick = {}
            )
        }
    }
}


@Composable
fun YourSongsSection(
    localSongState: HomeViewModel.LocalSongState,
    onSeeAllClick: () -> Unit
) {

    val mediaControllerManager = LocalMediaControllerManager.current
    val menuState = LocalMenuState.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Songs",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (localSongState is HomeViewModel.LocalSongState.Success &&
                localSongState.songs.isNotEmpty()
            ) {
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.clickable(onClick = onSeeAllClick)
                )
            }
        }

        // Content based on state
        when (localSongState) {
            is HomeViewModel.LocalSongState.Idle -> {
                // Empty or initial state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No songs loaded yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            is HomeViewModel.LocalSongState.Loading -> {
                // Loading state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeViewModel.LocalSongState.Success -> {
                if (localSongState.songs.isEmpty()) {
                    // No songs found
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No songs found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                } else {
                    // Display songs (limit to 5)
                    localSongState.songs.take(5).forEachIndexed { index, song ->
                        SongItemContent(
                            song = song,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            onSongClick = {
                                mediaControllerManager.playQueue(
                                    songs = localSongState.songs,
                                    index = index,
                                    id = localSongState.lastReloadMillis.toString()
                                )
                            },
                            onMoreChoice = {
                                menuState.show {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                            .background(color = MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        )
                    }
                }
            }

            is HomeViewModel.LocalSongState.Error -> {
                // Error state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${localSongState.message}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}