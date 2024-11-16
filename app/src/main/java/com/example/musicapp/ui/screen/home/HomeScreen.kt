package com.example.musicapp.ui.screen.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.constants.SongItemHeight
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.ui.components.LazyColumnWithAnimation2
import com.example.musicapp.ui.components.MyListItem
import com.example.musicapp.ui.components.Thumbnail
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.HomeViewModel
import com.example.musicapp.viewmodels.SongViewModel

@ExperimentalMaterial3Api
@UnstableApi
@Preview
@Composable
fun Preview() {
    MusicTheme {
        HomeScreen(
            viewModel = FakeModule.provideMainViewModel(),
            homeViewModel = FakeModule.provideHomeViewModel()
        )
    }
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: SongViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var permissionGranted by rememberSaveable {
        mutableStateOf(
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted)
            homeViewModel.loadSong()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }


    val isLoading by homeViewModel.isLoading.collectAsStateWithLifecycle()
    val songs by homeViewModel.songs.collectAsState()
    val currentSong by viewModel.activeSong.collectAsState()
    val playBackState by viewModel.playBackState.collectAsState()

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
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
                        0 -> viewModel.updatePlaylistState()
                        1 -> viewModel.playPreviousTrack()
                        2 -> viewModel.togglePlayback()
                        else -> viewModel.playNextTrack()
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
            val hidden = true
            if (!hidden) {
                IconButton(onClick = { viewModel.uploadSongs() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
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
            .background(MaterialTheme.colorScheme.background)

        Box(modifier = lazyColumnModifier) {
            if (permissionGranted) {
                LazyColumnWithAnimation2(modifier = lazyColumnModifier,
                    items = songs,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    key = { _, item -> item.id }) { itemModifier, index, song ->
                    SongItem(itemModifier.clickable { homeViewModel.play(index) }, song)
                }
            } else {
                if (isLoading)
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(R.drawable.icon), contentDescription = null)
                    Text(
                        text = "You must grant permission to access your devices media files in order to continue",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(
                        onClick = {
                            Log.d("PHUC", "HomeScreen: sdl")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                            } else {
                                permissionGranted = true
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Text(
                            text = "Allow",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SongItem(
    modifier: Modifier = Modifier, song: Song
) {
    MyListItem(
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        },
        leadingContent = {
            val imageModifier = Modifier
                .clip(commonShape)
                .fillMaxHeight()
                .aspectRatio(1f)
            Thumbnail(modifier = imageModifier, thumbnailSource = song.thumbnailSource)
        },
        supportingContent = {
            Text(
                text = "${song.artist} \u00B7 ${song.durationMillis.toDurationString()}",
                modifier.padding(top = 8.dp),
            )
        },
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        modifier = modifier
            .height(SongItemHeight)
            .background(MaterialTheme.colorScheme.background)
    )
}

//@Preview
@Composable
private fun SongItemPreview() {
    MusicTheme {
        SongItem(song = Song.unidentifiedSong())
    }
}