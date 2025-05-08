package com.example.musicapp.music.presentation.ui.screen.local

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.LocalMenuState
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.core.presentation.components.SongItemContent
import com.example.musicapp.ui.theme.darkGray
import com.example.musicapp.ui.theme.lightGray
import com.example.musicapp.ui.theme.white

@Composable
fun LocalSongsScreen(
    localViewModel: LocalViewModel,
    onBackPressed: () -> Unit = {}
) {
    val songState by localViewModel.songState.collectAsState()
    val scrollState = rememberLazyListState()
    val mediaControllerManager = LocalMediaControllerManager.current
    val menuState = LocalMenuState.current

    LaunchedEffect(Unit) {
        localViewModel.loadSongs()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkGray)
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = white
                )
            }

            Text(
                text = "Your Songs",
                style = MaterialTheme.typography.headlineMedium,
                color = white,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Main content based on state
        when (songState) {
            is LocalViewModel.SongState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LocalViewModel.SongState.Success -> {
                val songs = (songState as LocalViewModel.SongState.Success).songs
                if (songs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No local songs found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = lightGray
                        )
                    }
                } else {
                    LazyColumnWithAnimation2(
                        state = scrollState,
                        items = songs,
                        key = { index, item -> item.id },
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) { itemModifier, index, song ->
                        SongItemContent(
                            song = song,
                            modifier = itemModifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            onSongClick = {
                                mediaControllerManager?.playQueue(
                                    songs = songs,
                                    index = index,
                                    id = "local_songs"
                                )
                            },
                            onMoreChoice = {
                                menuState.show {
                                    // Menu content here - similar to YourSongsSection
                                }
                            }
                        )
                    }
                }
            }

            is LocalViewModel.SongState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${(songState as LocalViewModel.SongState.Error).message}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                // Idle state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading your songs...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = lightGray
                    )
                }
            }
        }
    }
}