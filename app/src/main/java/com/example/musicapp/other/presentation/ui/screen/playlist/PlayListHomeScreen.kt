package com.example.musicapp.other.presentation.ui.screen.playlist

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.R
import com.example.musicapp.constants.PlayListHeight
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.core.presentation.components.MyTextField
import com.example.musicapp.di.FakeModule
import com.example.musicapp.core.presentation.theme.Black
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import com.example.musicapp.song.presetation.components.Screens

@Preview
@Composable
private fun PlayListHomePreview() {
    MusicTheme {
        PlayListHome(rememberNavController(), FakeModule.providePlaylistViewModel())
    }
}

@Composable
fun PlayListHome(navController: NavHostController, viewModel: PlaylistViewModel) {

    val playlists by viewModel.playlists.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val dismissCreatePlaylist = {
        showDialog = false
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = null
                )
            }
        }
    ) { contentPadding ->

        val contentModifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Black)

        LazyColumn(
            modifier = contentModifier
        ) {
            itemsIndexed(items = playlists, key = { _, item ->
                item.data.id
            }) { _, item ->
                ListItem(
                    leadingContent = {
                        Image(
                            painter = painterResource(R.drawable.image), contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(DefaultCornerSize))
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        )
                    },
                    headlineContent = {
                        Text(
                            text = item.data.name,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PlayListHeight)
                        .animateItem(
                            fadeInSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            placementSpec = spring(
                                stiffness = Spring.StiffnessMediumLow,
                                visibilityThreshold = IntOffset.VisibilityThreshold
                            ),
                            fadeOutSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                navController.navigate(Screens.PlaylistDetail.route + "/" + item.data.id)
                            }, onLongPress = {
                                viewModel.togglePlayList(item.data.id)
                            })
                        },
                    trailingContent = {
                        if (item.isSelected) {
                            CommonIcon(
                                icon = R.drawable.ic_delete, tint = MaterialTheme.colorScheme.error
                            ) { viewModel.deletePlayList() }
                        }
                    },
                    colors = ListItemDefaults.colors(
                        headlineColor = MaterialTheme.colorScheme.primary,
                        overlineColor = MaterialTheme.colorScheme.primary,
                        containerColor = Black,
                        supportingColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.primary,
                        trailingIconColor = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }

        if (showDialog) {
            var name by remember { mutableStateOf("Unnamed") }
            AlertDialog(modifier = Modifier,
                containerColor = MaterialTheme.colorScheme.background,
                onDismissRequest = {
                    dismissCreatePlaylist()
                },
                text = {
                    MyTextField(
                        value = name,
                        onValueChange = { name = it },
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
                }
            )
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


