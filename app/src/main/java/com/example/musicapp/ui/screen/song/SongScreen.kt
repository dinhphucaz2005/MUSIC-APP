package com.example.musicapp.ui.screen.song

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.AppViewModel
import com.example.musicapp.ui.screen.edit.EditScreen
import com.example.musicapp.ui.screen.edit.EditViewModel
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape

@UnstableApi
@Preview
@Composable
fun SongScreenPreview() {
    MusicTheme {
        SongScreen(
            onDismiss = {},
            viewModel = FakeModule.provideViewModel(),
            editViewModel = FakeModule.provideEditViewModel()
        )
    }
}

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    viewModel: AppViewModel,
    editViewModel: EditViewModel = hiltViewModel()
) {
    val isPlaying by viewModel.isPlaying().collectAsState()
    val thumbnail by viewModel.getThumbnail().collectAsState()
    val title by viewModel.getTitle().collectAsState()
    val artist by viewModel.getArtist().collectAsState()
    val playListState by viewModel.getPlayListState().collectAsState()
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val painter = painterResource(id = R.drawable.image)
    val iconSize = 32.dp

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (backgroundRef, boxRef, backButton, editButton, titleRef, artistRef, image, sliderRef, startTime, endTime, bottomRow) = createRefs()

        val backgroundModifier = Modifier
            .fillMaxSize()
            .constrainAs(backgroundRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }

        thumbnail?.let {
            Image(
                bitmap = it,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = backgroundModifier.blur(50.dp)
            )
        } ?: Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = backgroundModifier.blur(50.dp)
        )


        Box(
            modifier = Modifier
                .constrainAs(boxRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
        )

        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .size(iconSize)
                .clickable { onDismiss() }
                .constrainAs(backButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
            tint = MaterialTheme.colorScheme.primary
        )

        var showEditScreen by remember { mutableStateOf(false) }

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier
                .size(iconSize)
                .clickable { showEditScreen = true }
                .constrainAs(editButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            tint = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomRow) {
                    bottom.linkTo(parent.bottom, margin = 60.dp)
                }
        ) {
            val iconModifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                IconButton(
                    onClick = { viewModel.rewind() },
                    modifier = iconModifier
                ) { Icon(painter = painterResource(id = R.drawable.rewind)) }
                IconButton(
                    onClick = { viewModel.togglePlayPause() },
                    modifier = iconModifier
                ) { Icon(painter = painterResource(id = isPlaying.resource)) }
                IconButton(
                    onClick = { viewModel.fastForward() },
                    modifier = iconModifier
                ) { Icon(painter = painterResource(id = R.drawable.fast_fwd)) }
            }
            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(
                    onClick = { viewModel.refreshPlayListState() },
                    modifier = iconModifier
                ) { Icon(painterResource(id = playListState.resource)) }
                IconButton(
                    onClick = { viewModel.playPreviousTrack() },
                    modifier = iconModifier
                ) { Icon(painter = painterResource(id = R.drawable.skip_back)) }
                IconButton(
                    onClick = { viewModel.skipToNextTrack() },
                    modifier = iconModifier
                ) { Icon(painter = painterResource(id = R.drawable.skip_fwd)) }
                IconButton(
                    onClick = { TODO("Not yet implemented") },
                    modifier = iconModifier
                ) { Icon(painter = painterResource(id = R.drawable.home)) }
                IconButton(
                    onClick = { TODO("Not yet implemented") },
                    modifier = iconModifier
                ) { Icon(painter = painterResource(id = R.drawable.share)) }
            }
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .constrainAs(titleRef) {
                    top.linkTo(backButton.bottom, margin = 16.dp)
                    start.linkTo(backButton.start)
                    end.linkTo(editButton.end)
                }
                .basicMarquee(
                    delayMillis = 0,
                    iterations = Int.MAX_VALUE,
                    spacing = MarqueeSpacing.fractionOfContainer(1f / 10f)
                )
        )

        Text(
            text = artist,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.constrainAs(artistRef) {
                top.linkTo(titleRef.bottom, margin = 12.dp)
                start.linkTo(titleRef.start)
                end.linkTo(titleRef.end)
            }
        )

        val boxModifier = Modifier
            .padding(16.dp)
            .clip(commonShape)
            .fillMaxWidth()
            .aspectRatio(1f)
            .constrainAs(image) {
                top.linkTo(artistRef.top)
                bottom.linkTo(sliderRef.top)
            }
            .aspectRatio(1f)
            .border(
                width = 10.dp,
                color = MaterialTheme.colorScheme.primary.copy(0.7f),
                shape = commonShape
            )
            .padding(10.dp)

        Box(modifier = boxModifier) {
            val imageModifier = Modifier.fillMaxSize()
            thumbnail?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.image),
                contentDescription = null,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }


        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = { viewModel.seekToPosition(sliderPosition) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(0.5f)
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(sliderRef) {
                    start.linkTo(backButton.start)
                    end.linkTo(editButton.end)
                    bottom.linkTo(bottomRow.top, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
        )

        val startText = "00:00"
        val endText by viewModel.getDuration().collectAsState()
        Text(
            text = startText,
            modifier = Modifier.constrainAs(startTime) {
                top.linkTo(sliderRef.bottom)
                bottom.linkTo(sliderRef.bottom)
                start.linkTo(sliderRef.start)
            },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = endText,
            modifier = Modifier.constrainAs(endTime) {
                top.linkTo(sliderRef.bottom)
                bottom.linkTo(sliderRef.bottom)
                end.linkTo(sliderRef.end)
            },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall
        )



        AnimatedVisibility(
            visible = showEditScreen,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(
                    durationMillis = 600,
                    easing = EaseInOut
                )
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = EaseInOut
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(
                    durationMillis = 600,
                    easing = EaseInOut
                )
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = EaseInOut
                )
            )
        ) {
            EditScreen(
                song = viewModel.getCurrentSong(),
                onDismiss = { showEditScreen = false },
                viewModel = editViewModel
            )
        }
    }
}

@Composable
fun Icon(painter: Painter) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        tint = MaterialTheme.colorScheme.primary
    )
}