package com.example.musicapp.other.presentation.ui.screen.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
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
        Column {
            HomeContent(
                mediaControllerManager = FakeModule.provideMediaControllerManager(),
                songs = List(20) { index -> Song.unidentifiedSong(index.toString()) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@SuppressLint("InlinedApi")
@UnstableApi
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {
    var isReadMediaAudiosPermissionGranted by remember { mutableStateOf(false) }

    val readMediaAudiosPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> isReadMediaAudiosPermissionGranted = isGranted }
    )

    val requestReadMediaAudiosPermission = {
        readMediaAudiosPermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
    }

    LaunchedEffect(isReadMediaAudiosPermissionGranted) {
        if (!isReadMediaAudiosPermissionGranted) {
            requestReadMediaAudiosPermission()
        } else {
            homeViewModel.loadSongs()
        }
    }

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val isLoading by homeViewModel.isLoading.collectAsStateWithLifecycle()

    val playBackState by mediaControllerManager.playBackState.collectAsState()
    val currentSong by mediaControllerManager.currentSong.collectAsState()
    val songs by homeViewModel.songs.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MyListItem(
            modifier = Modifier
                .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth()
                .height(120.dp),
            headlineContent = {
                Text(
                    text = currentSong.getTitle(),
                    style = MaterialTheme.typography.titleLarge,
                    color = White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            },
            supportingContent = {
                Text(
                    text = currentSong.getArtistName(),
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
                    thumbnailSource = currentSong.getThumbnail()
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
        if (!isReadMediaAudiosPermissionGranted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            ) {
                Text(
                    text = "Please grant the permission to read media audios",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
                Button(onClick = { requestReadMediaAudiosPermission() }) {
                    Text(text = "Grant Permission")
                }
            }
        } else {
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
                    songs = songs,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }



}

@Composable
private fun ColumnScope.HomeContent(
    modifier: Modifier = Modifier,
    mediaControllerManager: MediaControllerManager,
    songs: List<Song>,
) {

    LazyColumnWithAnimation2(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f),
        items = songs,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        key = { _, item -> item.id }
    ) { itemModifier, index, song ->
        SongItemContent(
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

@Composable
fun SongItemContent(
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
        SongItemContent(song = Song.unidentifiedSong())
    }
}