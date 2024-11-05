package com.example.musicapp.ui.screen.song

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.musicapp.R
import com.example.musicapp.constants.IconSize
import com.example.musicapp.constants.TopBarHeight
import com.example.musicapp.extension.toDuration
import com.example.musicapp.ui.components.CommonIcon
import com.example.musicapp.ui.components.CustomSlider
import com.example.musicapp.ui.components.Thumbnail
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@UnstableApi
@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel,
    popSongScreen: () -> Unit
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val playBackState by viewModel.playBackState.collectAsState()

    var pause by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf("00:00") }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(pause) {
        if (!pause) while (true) {
            CoroutineScope(Dispatchers.Main).launch {
                sliderPosition = viewModel.getCurrentSliderPosition()
                current = viewModel.getCurrentTrackPosition().toDuration()
            }
            delay(1000)
        }
    }

    var isPopBackStack by remember { mutableStateOf(false) }
    var showEditScreen by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Thumbnail(
            Modifier
                .fillMaxSize()
                .blur(50.dp), currentSong.smallBitmap
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(TopBarHeight)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }, modifier = Modifier.size(IconSize)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        modifier = modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = { showEditScreen = true }, modifier = Modifier.size(IconSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = currentSong.title,
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
                text = currentSong.author,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
            )

            val boxBorderWidth = 10.dp

            Box(
                modifier = Modifier
                    .clip(commonShape)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(
                        width = boxBorderWidth, color = MaterialTheme.colorScheme.primary.copy(0.7f)
                    )
                    .padding(boxBorderWidth)
            ) {
                Thumbnail(
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                if (dragAmount.y >= 25 && !isPopBackStack) {
                                    isPopBackStack = true
                                    popSongScreen()
                                }
                            }
                        }, currentSong.smallBitmap
                )
            }

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
                    text = currentSong.duration.toDuration(),
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
                IconButton(
                    onClick = { viewModel.rewindTrack() }, modifier = iconModifier
                ) { CommonIcon(painter = painterResource(id = R.drawable.ic_rewind)) }
                IconButton(
                    onClick = { viewModel.togglePlayback() }, modifier = iconModifier
                ) { CommonIcon(painter = painterResource(id = playBackState.playerState.resource)) }
                IconButton(
                    onClick = { viewModel.fastForwardTrack() }, modifier = iconModifier
                ) { CommonIcon(painter = painterResource(id = R.drawable.ic_fast_forward)) }
            }

            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(
                    onClick = { viewModel.updatePlaylistState() }, modifier = iconModifier
                ) { CommonIcon(painterResource(id = playBackState.loopMode.resource)) }
                IconButton(
                    onClick = { viewModel.playPreviousTrack() }, modifier = iconModifier
                ) { CommonIcon(painter = painterResource(id = R.drawable.ic_skip_back)) }
                IconButton(
                    onClick = { viewModel.playNextTrack() }, modifier = iconModifier
                ) { CommonIcon(painter = painterResource(id = R.drawable.ic_skip_forward)) }
                IconButton(
                    onClick = { TODO("Not yet implemented") }, modifier = iconModifier
                ) { CommonIcon(painter = painterResource(id = R.drawable.ic_setting)) }
                IconButton(
                    onClick = { TODO("Not yet implemented") }, modifier = iconModifier
                ) { CommonIcon(painter = painterResource(id = R.drawable.ic_share)) }
            }
        }


//        AnimatedVisibility(
//            visible = showEditScreen,
//            enter = Animator.enterAnimation,
//            exit = Animator.exitAnimation
//        ) {
//            EditScreen(song = currentSong, onDismiss = { showEditScreen = false })
//        }
    }
}

