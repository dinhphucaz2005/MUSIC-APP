package com.example.musicapp.ui.screen.song

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.R
import com.example.musicapp.constants.IconSize
import com.example.musicapp.constants.TopBarHeight
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.ui.animation.Animator
import com.example.musicapp.ui.components.CommonIcon
import com.example.musicapp.ui.components.CustomSlider
import com.example.musicapp.ui.components.Thumbnail
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.SongViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun SongScreenPreview(modifier: Modifier = Modifier) {
    MusicTheme {
        SongScreen(
            navController = rememberNavController(), viewModel = FakeModule.provideSongViewModel()
        )
    }
}

@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SongViewModel
) {
    val activeSong by viewModel.activeSong.collectAsState()
    val playBackState by viewModel.playBackState.collectAsState()

    var pause by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf("00:00") }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(pause) {
        if (!pause) while (true) {
            CoroutineScope(Dispatchers.Main).launch {
                sliderPosition = viewModel.getCurrentSliderPosition()
                current = viewModel.getCurrentTrackPosition().toDurationString()
            }
            delay(100)
        }
    }

    var showEditScreen by remember { mutableStateOf(false) }

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
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(TopBarHeight)
            ) {
                CommonIcon(icon = R.drawable.ic_back) { navController.popBackStack() }

                Spacer(Modifier.weight(1f))

                CommonIcon(icon = R.drawable.ic_edit) { showEditScreen = true }
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

            var isPopBackStack = false

            FlippingBox(
                Modifier
                    .clip(commonShape)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            if (dragAmount.y >= 25 && !isPopBackStack) {
                                isPopBackStack = true
                                navController.popBackStack()
                            }
                        }
                    }, activeSong
            )

            CustomSlider(modifier = modifier.fillMaxWidth(),
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    pause = true
                },
                onValueChangeFinished = {
                    viewModel.seekToSliderPosition(sliderPosition)
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
                    iconModifier,
                    R.drawable.ic_rewind,
                ) { viewModel.rewindTrack() }
                CommonIcon(
                    modifier = iconModifier,
                    playBackState.playerState.resource,
                ) { viewModel.togglePlayback() }
                CommonIcon(
                    iconModifier,
                    R.drawable.ic_fast_forward,
                ) { viewModel.fastForwardTrack() }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                CommonIcon(
                    iconModifier,
                    playBackState.loopMode.resource
                ) { viewModel.updatePlaylistState() }
                CommonIcon(
                    iconModifier,
                    R.drawable.ic_skip_back,
                ) { viewModel.playPreviousTrack() }
                CommonIcon(
                    iconModifier,
                    R.drawable.ic_skip_forward,
                ) { viewModel.playNextTrack() }
                CommonIcon(iconModifier, R.drawable.ic_setting) { TODO() }
                CommonIcon(iconModifier, R.drawable.ic_upload) { TODO() }
            }
        }
    }
    AnimatedVisibility(
        visible = showEditScreen,
        modifier = Modifier.fillMaxSize(),
        enter = Animator.enterAnimation,
        exit = Animator.exitAnimation
    ) {
        TODO("Implement edit screen")
//            EditScreen(oldSong = activeSong, onDismiss = { showEditScreen = false })
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