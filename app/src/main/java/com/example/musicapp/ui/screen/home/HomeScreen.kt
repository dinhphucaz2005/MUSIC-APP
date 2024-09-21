@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.musicapp.ui.screen.home

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.MainViewModel
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape

@ExperimentalMaterial3Api
@UnstableApi
@Preview
@Composable
fun Preview() {
    MusicTheme {
        HomeScreen(viewModel = FakeModule.provideViewModel())
    }
}


@ExperimentalMaterial3Api
@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: MainViewModel
) {
    val songs = viewModel.songList
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()
    val playlistState by viewModel.playlistState.collectAsState()

    val thumbnail = painterResource(id = R.drawable.image)

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {

        val (image, textRef, row, divider, lazyColumn) = createRefs()


        val imageModifier = Modifier
            .clip(commonShape)
            .size(120.dp)
            .constrainAs(image) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 12.dp)
            }

        currentSong.smallBitmap?.let {
            Image(
                contentScale = ContentScale.Crop,
                bitmap = it,
                contentDescription = null,
                modifier = imageModifier
            )
        } ?: Image(
            painter = thumbnail,
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
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
                softWrap = true,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = currentSong.author,
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
                playlistState.resource,
                R.drawable.ic_skip_back,
                isPlaying.resource,
                R.drawable.ic_skip_forward
            ).forEachIndexed { index, resId ->
                IconButton(onClick = {
                    when (index) {
                        0 -> viewModel.refreshPlayListState()
                        1 -> viewModel.playPreviousTrack()
                        2 -> viewModel.togglePlayPause()
                        else -> viewModel.skipToNextTrack()
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
                IconButton(onClick = {
                    viewModel.upload()
                }) {
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
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(lazyColumn) {
                top.linkTo(divider.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }) {
            val songItemModifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(vertical = 8.dp, horizontal = 4.dp)
            items(songs.size) { index ->
                SongItem(songItemModifier.clickable {
                    viewModel.playTrackAtIndex(index)
                }, songs[index])
            }
        }
    }
}

