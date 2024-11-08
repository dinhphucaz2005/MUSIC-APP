package com.example.musicapp.ui.screen.playlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.ui.components.CommonImage
import com.example.musicapp.ui.components.LazyColumnWithAnimation
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.PlayListDetailViewModel

@Preview
@Composable
fun PlaylistDetailPreview() {
    MusicTheme {
        PlayListDetail(
            navController = NavHostController(LocalContext.current),
            viewModel = FakeModule.providePlaylistDetailViewModel(),
            playlistId = 1
        )
    }
}

@Composable
fun PlayListDetail(
    navController: NavHostController,
    viewModel: PlayListDetailViewModel,
    playlistId: Long?,
) {
    LaunchedEffect(playlistId) {
        viewModel.loadPlayList(playlistId)
    }

    val playList by viewModel.playList.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()
    val inSelectionMode by viewModel.inSelectionMode.collectAsState()

    val thumbnail = painterResource(id = R.drawable.image)


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                "${selectedItems.size} selected",
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
            )
            Checkbox(true, onCheckedChange = {})
            Icon(
                imageVector = Icons.Default.MoreVert, modifier = Modifier.clickable {
                    TODO("Show more options")
                }, tint = MaterialTheme.colorScheme.primary, contentDescription = null
            )
        }

        CommonImage(
            bitmap = playList?.songs?.firstOrNull()?.thumbnail,
            painter = thumbnail,
            modifier = Modifier
                .padding(8.dp)
                .clip(commonShape)
                .border(
                    width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = commonShape
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
                text = playList?.name ?: "Unknown",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Playlist - ${playList?.getSong()?.size ?: 0} songs",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
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
                        viewModel.deleteSelectedSongs()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(onClick = { viewModel.exitSelectionMode() }) {
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
            onClick = { TODO("Shuffle all songs") },
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
            onClick = { TODO("Play list") },
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

        LazyColumnWithAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(lazyRow) {
                    top.linkTo(horizontalDivider.bottom, margin = 12.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }, items = playList?.songs ?: emptyList()
        ) { itemModifier, index, item ->
            val song = item as Song
            val isSelected = selectedItems[song.id] ?: false
            Row(
                modifier = itemModifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .clip(commonShape)
                    .background(if (isSelected) Color(0xFF48576e) else Color.Transparent)
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = {
                            viewModel.startSelectionMode()
                            viewModel.toggleSelection(song.id)
                        }, onTap = {
                            if (inSelectionMode) {
                                viewModel.toggleSelection(song.id)
                            } else viewModel.playTrack(index)
                        })
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                CommonImage(
                    bitmap = song.thumbnail,
                    painter = thumbnail,
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