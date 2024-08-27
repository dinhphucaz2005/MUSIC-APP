package com.example.musicapp.ui.screen.playlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.dragselectcompose.grid.indicator.internal.RadioButtonUnchecked
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.extension.getFileNameWithoutExtension
import com.example.musicapp.ui.components.CommonImage
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape

@UnstableApi
@Preview
@Composable
fun PlaylistDetailPreview() {
    MusicTheme {
        PlaylistDetail(
            navController = NavHostController(LocalContext.current),
            viewModel = FakeModule.providePlaylistViewModel(),
            playlistId = 1
        )
    }
}

@UnstableApi
@Composable
fun PlaylistDetail(
    navController: NavHostController,
    viewModel: PlaylistViewModel,
    playlistId: Long?,
) {
    LaunchedEffect(playlistId) {
        viewModel.setCurPlaylist(playlistId)
    }


    val curPlaylist by viewModel.curPlaylist.collectAsState()
    val thumbnail = painterResource(id = R.drawable.image)

    val selectedSongs = remember { mutableStateListOf<Boolean>() }


    var inSelectionMode by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (thumbnailRef, titleRef) = createRefs()
        val verticalChain = createGuidelineFromStart(0.5f)

        CommonImage(
            bitmap = curPlaylist?.songs?.firstOrNull()?.thumbnail,
            painter = thumbnail,
            modifier = Modifier
                .padding(8.dp)
                .clip(commonShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = commonShape
                )
                .constrainAs(thumbnailRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(verticalChain)
                    width = Dimension.fillToConstraints
                }
                .aspectRatio(1f),
        )

        Column(
            modifier = Modifier
                .constrainAs(titleRef) {
                    top.linkTo(thumbnailRef.top)
                    start.linkTo(verticalChain)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .aspectRatio(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = curPlaylist?.name ?: "Unknown",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Text(
                text = "Playlist - ${curPlaylist?.songs?.size ?: 0} songs",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                IconButton(onClick = { navController.navigate(Routes.PLAYLIST_EDIT.name + "/" + playlistId) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                if (inSelectionMode) {
                    IconButton(onClick = {
                        viewModel.deleteSelectedSongs(selectedSongs)
                        inSelectionMode = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(onClick = {
                        inSelectionMode = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        val (button1, button2) = createRefs()
        Button(
            onClick = { TODO("Not implemented yet") },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .constrainAs(button1) {
                    top.linkTo(thumbnailRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(verticalChain)
                    width = Dimension.fillToConstraints
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(text = "Shuffle")
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .constrainAs(button2) {
                    top.linkTo(button1.top)
                    start.linkTo(verticalChain)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(text = "Play")
        }


        val (horizontalDivider, lazyRow) = createRefs()

        HorizontalDivider(modifier = Modifier.constrainAs(horizontalDivider) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(button1.bottom, margin = 12.dp)
            width = Dimension.fillToConstraints
        }, color = MaterialTheme.colorScheme.primary)

        curPlaylist?.songs?.let { songs ->
            selectedSongs.apply {
                clear()
                addAll(List(songs.size) { false })
            }
            val songItemModifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(vertical = 8.dp, horizontal = 4.dp)
            val thumbnailModifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
            val toggleSelectionSong: (Int) -> Unit = { index ->
                selectedSongs[index] = !selectedSongs[index]
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lazyRow) {
                        top.linkTo(horizontalDivider.bottom, margin = 12.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                itemsIndexed(items = songs) { index, song ->
                    Row(
                        modifier = songItemModifier
                            .clip(commonShape)
                            .background(
                                if (selectedSongs[index]) Color(0xFF48576e)
                                else Color.Transparent
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        inSelectionMode = true
                                        selectedSongs[index] = true
                                    },
                                    onTap = {
                                        if (inSelectionMode)
                                            toggleSelectionSong(index)
                                        else {
                                            viewModel.playTrackAtIndex(index)
                                        }
                                    }
                                )
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CommonImage(
                            bitmap = song.smallBitmap,
                            painter = thumbnail,
                            modifier = thumbnailModifier
                        )
                        Text(
                            text = song.fileName.getFileNameWithoutExtension(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        if (inSelectionMode) {
                            Icon(
                                imageVector = if (selectedSongs[index]) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                tint = MaterialTheme.colorScheme.primary, contentDescription = null,
                                modifier = Modifier
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}