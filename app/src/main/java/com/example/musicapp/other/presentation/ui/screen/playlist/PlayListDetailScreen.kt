package com.example.musicapp.other.presentation.ui.screen.playlist

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.dragselectcompose.grid.indicator.internal.RadioButtonUnchecked
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.other.viewmodels.PlaylistViewModel

@SuppressLint("UnsafeOptInUsageError")
@Preview
@Composable
fun PlaylistDetailPreview() {
    MusicTheme {
        PlayListDetail(
            playlistId = "5092",
            navController = NavHostController(LocalContext.current),
            viewModel = FakeModule.providePlaylistViewModel(),
        )
    }
}

@Composable
fun PlayListDetail(
    playlistId: String?,
    navController: NavHostController,
    viewModel: PlaylistViewModel,
) {
    LaunchedEffect(playlistId) {
        if (playlistId == null) {
            navController.popBackStack()
        } else {
            viewModel.loadPlaylist(playlistId)
        }
    }

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val songs by viewModel.songs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val activePlayList by viewModel.activePlayList.collectAsState()

    var inSelectionMode by remember { mutableStateOf(false) }
    val selectedSongIds = remember { mutableStateListOf<String>() }

    val exitSelectionMode = {
        inSelectionMode = false
        selectedSongIds.clear()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) LoadingScreen(Modifier.fillMaxSize())
        else {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false, onClick = {})
            ) {
                val (topBar, thumbnailRef, titleRef, button1, button2, horizontalDivider, lazyRow) = createRefs()

                val guideLine = createGuidelineFromStart(0.5f)

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .constrainAs(topBar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    CommonIcon(icon = R.drawable.ic_back, onClick = navController::popBackStack)

                    if (inSelectionMode) Text(
                        "${selectedSongIds.size} selected",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                    ) else Spacer(Modifier.weight(1f))

                    Checkbox(true, onCheckedChange = {})

                    Icon(
                        imageVector = Icons.Default.MoreVert, modifier = Modifier.clickable {
                            /*TODO("Show more options")*/
                        }, tint = MaterialTheme.colorScheme.primary, contentDescription = null
                    )
                }

                Thumbnail(
                    thumbnailSource = songs.firstOrNull()?.thumbnailSource,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(DefaultCornerSize))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(DefaultCornerSize)
                        )
                        .constrainAs(thumbnailRef) {
                            top.linkTo(topBar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(guideLine)
                            width = Dimension.fillToConstraints
                        }
                        .aspectRatio(1f),
                )

                Column(modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(thumbnailRef.top)
                        start.linkTo(guideLine)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .aspectRatio(1f), verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = activePlayList?.name ?: "Unknown",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Playlist - ${songs.size} songs",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row {
                        IconButton(onClick = { navController.navigate(PlaylistRoute.EDIT + "/" + playlistId) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (inSelectionMode) {
                            IconButton(onClick = {
                                viewModel.deleteSongs(selectedSongIds.toList())
                                exitSelectionMode()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                            IconButton(onClick = { exitSelectionMode() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { /*TODO("Shuffle all songs")*/ },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .constrainAs(button1) {
                            top.linkTo(thumbnailRef.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(guideLine)
                            width = Dimension.fillToConstraints
                        },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_shuffle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Shuffle",
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        val queue = Queue.Builder()
                            .setId(playlistId.toString())
                            .setIndex(0)
                            .setOtherSongs(songs)
                            .build()
                        mediaControllerManager.playQueue(queue)
                    },
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .constrainAs(button2) {
                            top.linkTo(button1.top)
                            start.linkTo(guideLine)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                    Text(text = "Play", modifier = Modifier.padding(start = 8.dp))
                }

                HorizontalDivider(modifier = Modifier.constrainAs(horizontalDivider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(button1.bottom, margin = 12.dp)
                    width = Dimension.fillToConstraints
                }, color = MaterialTheme.colorScheme.primary)

                LazyColumnWithAnimation(modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lazyRow) {
                        top.linkTo(horizontalDivider.bottom, margin = 12.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    items = songs) { itemModifier, index, item ->
                    val song = item as Song
                    var isSelected = selectedSongIds.contains(song.id)
                    Row(
                        modifier = itemModifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(DefaultCornerSize))
                            .background(if (isSelected) Color(0xFF48576e) else Color.Transparent)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    if (!inSelectionMode) {
                                        inSelectionMode = true
                                        selectedSongIds.add(song.id)
                                    }
                                }, onTap = {
                                    if (inSelectionMode) {
                                        isSelected = !isSelected
                                        if (isSelected) selectedSongIds.remove(song.id)
                                        else selectedSongIds.add(song.id)
                                    } else {
                                        val queue = Queue
                                            .Builder()
                                            .setId(playlistId.toString())
                                            .setOtherSongs(songs)
                                            .setIndex(index)
                                            .build()
                                        mediaControllerManager.playQueue(queue)
                                    }
                                })
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Thumbnail(
                            thumbnailSource = song.thumbnailSource,
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        )
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                        if (inSelectionMode) {
                            Icon(
                                imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}