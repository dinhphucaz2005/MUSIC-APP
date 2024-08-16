@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.musicapp.ui.screen.home

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.getFileNameWithoutExtension
import com.example.musicapp.ui.AppViewModel
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape

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
    modifier: Modifier = Modifier,
    viewModel: AppViewModel
) {
    val songs = viewModel.songList
    val playingState by viewModel.isPlaying().collectAsState()
    val bitmap by viewModel.getThumbnail().collectAsState()
    val thumbnail = painterResource(id = R.drawable.image)

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {

        val image = createRef()

        val imageModifier = Modifier
            .clip(commonShape)
            .size(120.dp)
            .constrainAs(image) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 12.dp)
            }

        bitmap?.let {
            Image(
                contentScale = ContentScale.Crop,
                bitmap = it, contentDescription = null,
                modifier = imageModifier
            )
        } ?: Image(
            painter = thumbnail, contentDescription = null,
            modifier = imageModifier, contentScale = ContentScale.Crop
        )

        val textRef = createRef()

        Column(
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                    start.linkTo(image.end)
                    end.linkTo(parent.end, margin = 12.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = viewModel.getTitle().collectAsState().value,
                style = MaterialTheme.typography.titleLarge,
                softWrap = true,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = viewModel.getArtist().collectAsState().value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        val playListState by viewModel.getPlayListState().collectAsState()

        val row = createRef()
        Row(
            modifier = Modifier
                .constrainAs(row) {
                    top.linkTo(image.bottom)
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val iconModifier = Modifier
                .size(40.dp)
                .padding(8.dp)

            listOf(
                playListState.resource,
                R.drawable.skip_back,
                playingState.resource,
                R.drawable.skip_fwd
            ).forEachIndexed { index, resId ->
                IconButton(
                    onClick = {
                        when (index) {
                            0 -> viewModel.refreshPlayListState()
                            1 -> viewModel.playPreviousTrack()
                            2 -> viewModel.togglePlayPause()
                            else -> viewModel.skipToNextTrack()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = iconModifier,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        val divider = createRef()
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.constrainAs(divider) {
                top.linkTo(row.bottom)
            },
            color = MaterialTheme.colorScheme.onSecondary
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(createRef()) {
                    top.linkTo(divider.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
        ) {
            val songItemModifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(vertical = 8.dp, horizontal = 4.dp)
            items(songs.size) { index ->
                SongItem(songItemModifier.clickable {
                    viewModel.playTrackAtIndex(index)
                }, songs[index], thumbnail)
            }
        }
    }
}

@SuppressLint("PrivateResource")
@UnstableApi
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song,
    thumbnail: Painter
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .clip(commonShape)
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxHeight()
            .aspectRatio(1f)

        song.smallBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }
        if (song.smallBitmap == null) {
            Image(
                painter = thumbnail,
                contentDescription = null,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = song.fileName.getFileNameWithoutExtension(),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}