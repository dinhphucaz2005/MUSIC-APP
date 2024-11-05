@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.musicapp.ui.screen.home

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.viewmodels.MainViewModel
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape

@ExperimentalMaterial3Api
@UnstableApi
@Preview
@Composable
fun Preview() {
    MusicTheme {
        HomeScreen(viewModel = FakeModule.provideMainViewModel())
    }
}


@ExperimentalMaterial3Api
@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: MainViewModel
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val playList by viewModel.playList.collectAsState()
    val playBackState by viewModel.playBackState.collectAsState()

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
                text = currentSong.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
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
        SongList(
            Modifier
                .fillMaxWidth()
                .constrainAs(lazyColumn) {
                    top.linkTo(divider.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }, playList.songs
        ) { index ->
            viewModel.playSongAtIndex(index)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun SongList(modifier: Modifier, songs: List<Song>, onItemCLick: (index: Int) -> Unit) {
    val itemModifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(vertical = 8.dp, horizontal = 4.dp)
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(items = songs, key = { _, item -> item.id }) { index, song ->
            val offsetX = remember { Animatable(100f) }

            LaunchedEffect(song) {
                offsetX.animateTo(
                    targetValue = 0f, animationSpec = tween(durationMillis = 150)
                )
            }

            SongItem(
                itemModifier
                    .clickable { onItemCLick.invoke(index) }
                    .offset(x = offsetX.value.dp),
                song
            )
        }
    }
}

@SuppressLint("PrivateResource")
@UnstableApi
@Composable
fun SongItem(
    modifier: Modifier = Modifier, song: Song, colors: List<Color> = listOf()
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .clip(commonShape)
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxHeight()
            .aspectRatio(1f)

        if (song.smallBitmap != null) {
            Image(
                bitmap = song.smallBitmap,
                contentDescription = null,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = imageModifier.background(
                    brush = Brush.linearGradient(
                        if (colors.size >= 2) colors else
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                    )
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.author,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.primary,
            )
        }


        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
