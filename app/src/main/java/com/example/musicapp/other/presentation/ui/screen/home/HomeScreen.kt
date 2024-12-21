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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.constants.SongItemHeight
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.core.presentation.components.MyListItem
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.core.presentation.theme.White
import com.example.musicapp.di.FakeModule
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.other.domain.model.PlayBackState
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.util.MediaControllerManager

@ExperimentalMaterial3Api
@UnstableApi
@Preview
@Composable
fun Preview() {
    MusicTheme {
        HomeContent(
            mediaControllerManager = FakeModule.provideMediaControllerManager(),
            playBackState = FakeModule.providePlayBackState(),
            songs = List(20) { index -> Song.unidentifiedSong(index.toString()) },
            currentSong = Song.unidentifiedSong(),
            modifier = Modifier.fillMaxSize()
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

    val playBackState by mediaControllerManager.playBackState.collectAsState()
    val currentSong by mediaControllerManager.currentSong.collectAsState()
    val songs by homeViewModel.songs.collectAsState()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        HomeContent(
            mediaControllerManager = mediaControllerManager,
            playBackState = playBackState,
            songs = songs,
            currentSong = currentSong,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    mediaControllerManager: MediaControllerManager,
    playBackState: PlayBackState,
    songs: List<Song>,
    currentSong: Song,
) {
    Column(
        modifier = modifier
    ) {
        MyListItem(
            modifier = Modifier
                .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth()
                .height(120.dp),
            headlineContent = {
                Text(
                    text = currentSong.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            },
            supportingContent = {
                Text(
                    text = currentSong.artist,
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
            },
            leadingContent = {
                Thumbnail(
                    Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(DefaultCornerSize)),
                    contentScale = ContentScale.Crop,
                    thumbnailSource = currentSong.thumbnailSource
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
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
            thickness = 2.dp,
            color = White
        )

        LazyColumnWithAnimation2(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .weight(1f),
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
                .clip(RoundedCornerShape(DefaultCornerSize))
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