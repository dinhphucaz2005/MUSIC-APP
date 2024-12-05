package com.example.musicapp.other.presentation.ui.screen.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.constants.SongItemHeight
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.core.presentation.components.MyListItem
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.core.presentation.theme.White
import com.example.musicapp.core.presentation.theme.commonShape
import com.example.musicapp.di.FakeModule
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.viewmodels.HomeViewModel

@ExperimentalMaterial3Api
@UnstableApi
@Preview
@Composable
fun Preview() {
    MusicTheme {
        HomeScreen(
            homeViewModel = FakeModule.provideHomeViewModel()
        )
    }
}


@UnstableApi
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {
    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val isLoading by homeViewModel.isLoading.collectAsStateWithLifecycle()
    val songs by homeViewModel.songs.collectAsState()
    val queue by mediaControllerManager.queue.collectAsState()
    val playBackState by mediaControllerManager.playBackState.collectAsState()

    val currentSong = queue?.getCurrentSong() ?: Song.unidentifiedSong()

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (image, textRef, row, divider, lazyColumn) = createRefs()

        Thumbnail(
            Modifier
                .clip(commonShape)
                .size(120.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 12.dp)
                }, currentSong.thumbnailSource
        )

        Column(modifier = Modifier
            .constrainAs(textRef) {
                top.linkTo(image.top)
                bottom.linkTo(image.bottom)
                start.linkTo(image.end)
                end.linkTo(parent.end, margin = 12.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = currentSong.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
            Text(
                text = currentSong.artist,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }


        Row(modifier = Modifier
            .constrainAs(row) {
                top.linkTo(image.bottom)
            }
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val iconModifier = Modifier
                .size(40.dp)
                .padding(8.dp)

            listOf(
                playBackState.loopMode.resource,
                R.drawable.ic_skip_back,
                playBackState.playerState.resource,
                R.drawable.ic_skip_forward
            ).forEachIndexed { index, resId ->
                IconButton(onClick = {
                    when (index) {
                        0 -> mediaControllerManager.updatePlayListState()
                        1 -> mediaControllerManager.playPreviousSong()
                        2 -> mediaControllerManager.togglePlayPause()
                        else -> mediaControllerManager.playNextSong()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = iconModifier,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 2.dp, modifier = Modifier.constrainAs(divider) {
                top.linkTo(row.bottom)
            }, color = MaterialTheme.colorScheme.onSecondary
        )

        val lazyColumnModifier = Modifier
            .fillMaxWidth()
            .constrainAs(lazyColumn) {
                top.linkTo(divider.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }

        if (isLoading) {
            Box(modifier = lazyColumnModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumnWithAnimation2(modifier = lazyColumnModifier,
                items = songs,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                key = { _, item -> item.id }
            ) { itemModifier, index, song ->
                SongItem(
                    modifier = itemModifier.pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            mediaControllerManager.playQueue(
                                Queue
                                    .Builder()
                                    .setId(Queue.LOCAL_ID)
                                    .setIndex(index)
                                    .setOtherSongs(songs)
                                    .build()
                            )
                        })
                    },
                    song = song
                )
            }
        }

    }
}

@Composable
fun SongItem(
    modifier: Modifier = Modifier, song: Song,
) {
    MyListItem(
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = White,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        },
        leadingContent = {
            val imageModifier = Modifier
                .clip(commonShape)
                .fillMaxHeight()
                .aspectRatio(1f)
            Thumbnail(
                modifier = imageModifier,
                thumbnailSource = song.thumbnailSource
            )
        },
        supportingContent = {
            Text(
                text = "${song.artist} \u00B7 ${song.durationMillis.toDurationString()}",
                color = White
            )
        },
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = null,
                    tint = White,
                )
            }
        },
        modifier = modifier
            .height(SongItemHeight)
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun SongItemPreview() {
    MusicTheme {
        SongItem(song = Song.unidentifiedSong())
    }
}