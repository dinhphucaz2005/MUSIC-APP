package com.example.musicapp.ui.screen.playlist

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.ui.components.LazyColumnWithAnimation
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.screen.playlist.components.PlayListItem
import com.example.musicapp.ui.components.MyTextField
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.viewmodels.PlayListViewModel

@SuppressLint("UnsafeOptInUsageError")
@Preview
@Composable
private fun PlayListHomePreview() {
    MusicTheme {
        PlayListHome(rememberNavController(), FakeModule.providePlaylistViewModel())
    }
}

@Composable
fun PlayListHome(navController: NavHostController, viewModel: PlayListViewModel) {

    val playlists by viewModel.playlists.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val dismissCreatePlaylist = {
        showDialog = false
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp), floatingActionButton = {
        FloatingActionButton(onClick = {
            showDialog = true
        }, containerColor = MaterialTheme.colorScheme.tertiary) {
            Icon(
                imageVector = Icons.Outlined.AddCircle, contentDescription = null
            )
        }
    }) { contentPadding ->

        val contentModifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)

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
                                navController.navigate(Routes.PLAYLIST_DETAIL.name + "/" + (item as PlayList).id)
                            },
                            onLongPress = {
                                showDeleteButton = true
                            }
                        )
                    },
                playlist = item as PlayList,
                showDeleteButton = showDeleteButton,
                viewModel = viewModel
            )
        }
        if (showDialog) {
            var name by remember { mutableStateOf("") }
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

@Composable
fun LoadingScreen(contentModifier: Modifier) {
    Box(
        modifier = contentModifier, contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


