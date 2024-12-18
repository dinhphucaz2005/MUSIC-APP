package com.example.musicapp.song.presetation.song

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.constants.IconSize
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.constants.NavigationBarHeight
import com.example.musicapp.constants.PlayerState
import com.example.musicapp.constants.TopBarHeight
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.core.presentation.components.CustomSlider
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.core.presentation.components.MiniPlayer
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.presentation.ui.screen.home.SongItem
import com.example.musicapp.other.presentation.ui.theme.MusicTheme
import com.example.musicapp.other.presentation.ui.theme.Primary
import com.example.musicapp.other.presentation.ui.theme.commonShape
import com.example.musicapp.song.presetation.components.BottomSheet
import com.example.musicapp.song.presetation.components.BottomSheetState
import com.example.musicapp.song.presetation.components.rememberBottomSheetState
import com.example.musicapp.util.MediaControllerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomSheetPlayer(
    state: BottomSheetState,
    modifier: Modifier = Modifier,
) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    BottomSheet(
        state = state,
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
        onDismiss = { },
        collapsedContent = { MiniPlayer(state, mediaControllerManager) }
    ) { SongScreenContent(state, mediaControllerManager) }
}

@UnstableApi
@Preview
@Composable
private fun SongScreenContentPreview() {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val mediaControllerManager = MediaControllerManager(context, null, coroutineScope)
    val shouldShowNavigationBar = false

    val density = LocalDensity.current
    val windowsInsets = WindowInsets.systemBars
    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }


    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val playerBottomSheetState = rememberBottomSheetState(
            dismissedBound = 0.dp,
            collapsedBound = bottomInset + (if (shouldShowNavigationBar) NavigationBarHeight else 0.dp) + MiniPlayerHeight,
            expandedBound = maxHeight,
        )
        MusicTheme {
            SongScreenContent(playerBottomSheetState, mediaControllerManager)
        }
    }


}

@Composable
fun SongScreenContent(state: BottomSheetState, mediaControllerManager: MediaControllerManager) {

    val queue by mediaControllerManager.queue.collectAsState()

    val activeSong = queue?.getCurrentSong() ?: Song.unidentifiedSong()


    val playBackState by mediaControllerManager.playBackState.collectAsState()

    var pause by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf("00:00") }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(pause) {
        if (!pause) while (true) {
            CoroutineScope(Dispatchers.Main).launch {
                sliderPosition = mediaControllerManager.computePlaybackFraction() ?: 0f
                current = mediaControllerManager.getCurrentTrackPosition().toDurationString()
            }
            delay(100)
        }
    }

    var isQueueVisible by remember { mutableStateOf(false) }

    val toggleQueueVisibility = {
        isQueueVisible = !isQueueVisible
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Thumbnail(
            Modifier
                .fillMaxSize()
                .blur(50.dp),
            activeSong.thumbnailSource,
            contentScale = ContentScale.FillHeight
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TopBarHeight)
            ) {
                CommonIcon(icon = R.drawable.ic_back, onClick = state::collapseSoft)

                Spacer(Modifier.weight(1f))

                CommonIcon(icon = R.drawable.ic_edit, onClick = toggleQueueVisibility)
            }

            Text(
                text = activeSong.title,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE, spacing = MarqueeSpacing.fractionOfContainer(
                        1f / 10f
                    )
                )
            )

            Text(
                text = activeSong.artist,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
            )

            FlippingBox(
                Modifier
                    .clip(commonShape)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                activeSong
            )

            CustomSlider(
                modifier = Modifier.fillMaxWidth(),
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    pause = true
                },
                onValueChangeFinished = {
                    mediaControllerManager.seekToSliderPosition(sliderPosition)
                    pause = false
                })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = current,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = activeSong.durationMillis.toDurationString(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            val iconModifier = Modifier
                .fillMaxHeight()
                .size(IconSize)

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                CommonIcon(
                    modifier = iconModifier,
                    icon = R.drawable.ic_rewind,
                    onClick = mediaControllerManager::rewindTrack
                )

                val cornerSize by animateIntAsState(
                    targetValue = if (playBackState.playerState == PlayerState.PLAY) 50 else 12,
                    animationSpec = tween(durationMillis = 100), label = ""
                )

                Box(
                    modifier = Modifier
                        .clickable(onClick = mediaControllerManager::togglePlayPause)
                        .clip(RoundedCornerShape(cornerSize))
                        .background(Primary)
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(playBackState.playerState.resource),
                        contentDescription = null
                    )
                }


                CommonIcon(
                    modifier = iconModifier,
                    icon = R.drawable.ic_fast_forward,
                    onClick = mediaControllerManager::fastForwardTrack
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                CommonIcon(
                    modifier = iconModifier,
                    icon = playBackState.loopMode.resource,
                    onClick = mediaControllerManager::updatePlayListState
                )
                CommonIcon(
                    modifier = iconModifier,
                    icon = R.drawable.ic_skip_back,
                    onClick = mediaControllerManager::playPreviousSong
                )
                CommonIcon(
                    modifier = iconModifier,
                    icon = R.drawable.ic_skip_forward,
                    onClick = mediaControllerManager::playNextSong
                )
                CommonIcon(iconModifier, R.drawable.ic_setting)
                CommonIcon(iconModifier, R.drawable.ic_upload)
            }
        }
        AnimatedVisibility(
            visible = isQueueVisible,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.BottomCenter)
        ) {
            Column {
                HorizontalDivider(thickness = 5.dp, color = MaterialTheme.colorScheme.onSecondary)
                QueueView(queue, mediaControllerManager = mediaControllerManager)


            }
        }
    }
}

@Composable
fun QueueView(queue: Queue?, mediaControllerManager: MediaControllerManager) {
    when (queue) {
        is Queue.Youtube -> {
            LazyColumnWithAnimation2(
                items = queue.songs,
                modifier = Modifier.fillMaxSize(),
            ) { itemModifier, index, item ->
                com.example.musicapp.youtube.presentation.componenets.SongItem(
                    modifier = itemModifier, song = item, onClick = {
                        mediaControllerManager.playAtIndex(index)
                    }
                )
            }
        }

        is Queue.Other -> {
            LazyColumnWithAnimation2(
                items = queue.songs,
                modifier = Modifier.fillMaxSize(),
            ) { itemModifier, index, item ->
                SongItem(song = item, modifier = itemModifier.clickable {
                    mediaControllerManager.playAtIndex(index)
                })
            }
        }

        null -> return
    }
}


@Composable
fun FlippingBox(
    modifier: Modifier = Modifier, song: Song
) {
    var isActiveThumbnail by remember { mutableStateOf(true) }
    AnimatedContent(isActiveThumbnail, label = "", modifier = modifier.pointerInput(Unit) {
        detectTapGestures(onTap = {
            isActiveThumbnail = !isActiveThumbnail
        })
    }, transitionSpec = {
        val fadeInSpec = fadeIn(animationSpec = tween(durationMillis = 400))
        val scaleInSpec = scaleIn(
            initialScale = 0.85f, animationSpec = tween(
                durationMillis = 400, easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)
            )
        )
        val fadeOutSpec = fadeOut(animationSpec = tween(durationMillis = 400))

        (fadeInSpec + scaleInSpec).togetherWith(fadeOutSpec)
    }) { targetState ->
        val boxModifier = Modifier
            .clip(commonShape)
            .fillMaxSize()
        if (targetState) {
            Box(boxModifier) {
                Thumbnail(Modifier.fillMaxSize(), song.thumbnailSource)
            }
        } else {
            Box(boxModifier) {
                Text(
                    "Tap to change",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
