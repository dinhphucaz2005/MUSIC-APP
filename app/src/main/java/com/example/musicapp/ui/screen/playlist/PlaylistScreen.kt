package com.example.musicapp.ui.screen.playlist

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.ui.theme.MusicTheme

@Composable
@OptIn(UnstableApi::class)
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {

    val playlist by viewModel.playlistState.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (playlist.isNotEmpty()) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        modifier = Modifier.padding(vertical = 4.dp),
                        contentDescription = null
                    )
                    Text(text = "New playlist", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    ) { contentPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(paddingValues = contentPadding)
        ) {
            if (playlist.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(createRef()) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Playlists on your device will show up here",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddCircle,
                            modifier = Modifier.padding(vertical = 4.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                        Text(
                            text = "New playlist", modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    itemsIndexed(playlist) { _, item ->
                        PlayListItem(item)
                    }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun MyCustomAlertDialog(viewModel: PlaylistViewModel) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = { Button(onClick = { viewModel.savePlaylist() }) { Text(text = "Confirm") } },
        dismissButton = { Button(onClick = { viewModel.offDialog() }) { Text(text = "Dismiss") } },
        title = { Text(text = "Title") },
        text = { Text(text = "Text") },
        containerColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun PlayListItem(playList: Playlist = Playlist()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.primary)
        ) {
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = playList.name, color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
        Icon(
            imageVector = Icons.Default.PlayArrow, contentDescription = null,
            modifier = Modifier
                .padding(
                    vertical = 20.dp
                )
                .fillMaxHeight()
                .aspectRatio(1f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
