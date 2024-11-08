package com.example.musicapp.ui.screen.playlist

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.musicapp.R
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.ui.components.LazyColumnWithAnimation
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.screen.playlist.components.EmptyPlaylistScreen
import com.example.musicapp.ui.screen.playlist.components.PlayListItem
import com.example.musicapp.ui.screen.song.MyTextField
import com.example.musicapp.viewmodels.PlayListViewModel

@Composable
fun PlayListHome(navController: NavHostController, viewModel: PlayListViewModel) {

    val playlists by viewModel.playlists.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val thumbnail = painterResource(id = R.drawable.image)

    val dismissCreatePlaylist = {
        showDialog = false
        name = ""
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp), floatingActionButton = {
        if (playlists.isNotEmpty()) {
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    modifier = Modifier.padding(vertical = 4.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                Text(
                    text = "New playlist",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }) { contentPadding ->

        val contentModifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)

        if (playlists.isEmpty()) {
            EmptyPlaylistScreen(contentModifier) { showDialog = true }
        } else {
            LazyColumnWithAnimation(
                modifier = contentModifier,
                items = playlists,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) { itemModifier, _, item ->
                var showDeleteButton by remember { mutableStateOf(false) }

                PlayListItem(
                    modifier = itemModifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    navController.navigate(Routes.PLAYLIST_DETAIL.name + "/" + item.id)
                                },
                                onLongPress = {
                                    showDeleteButton = true
                                }
                            )
                        },
                    playlist = item as PlayList,
                    thumbnail = thumbnail,
                    showDeleteButton = showDeleteButton,
                    viewModel = viewModel
                )
            }
        }
        if (showDialog) {
            AlertDialog(modifier = Modifier,
                containerColor = MaterialTheme.colorScheme.background,
                onDismissRequest = {
                    dismissCreatePlaylist()
                },
                text = {
                    MyTextField(
                        value = name, onValueChange = { name = it }, label = "Playlist name"
                    )
                },
                dismissButton = {
                    Button(onClick = {
                        dismissCreatePlaylist()
                    }) {
                        Text(text = "Cancel", color = MaterialTheme.colorScheme.background)
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.createNewPlayList(name)
                        dismissCreatePlaylist()
                    }) {
                        Text(text = "Save", color = MaterialTheme.colorScheme.background)
                    }
                })
        }
    }
}


