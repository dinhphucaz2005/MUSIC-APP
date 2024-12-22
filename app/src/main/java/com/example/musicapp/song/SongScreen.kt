package com.example.musicapp.song

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.innertube.pages.RelatedPage
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.constants.NavigationBarHeight
import com.example.musicapp.constants.PlayerState
import com.example.musicapp.constants.TopBarHeight
import com.example.musicapp.core.presentation.components.BottomSheet
import com.example.musicapp.core.presentation.components.BottomSheetState
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.core.presentation.components.CustomSlider
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.core.presentation.components.LocalMenuState
import com.example.musicapp.core.presentation.components.MiniPlayer
import com.example.musicapp.core.presentation.components.MyListItem
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.core.presentation.components.rememberBottomSheetState
import com.example.musicapp.core.presentation.theme.LightGray
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.core.presentation.theme.Primary
import com.example.musicapp.core.presentation.theme.White
import com.example.musicapp.di.FakeModule
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.other.domain.model.CurrentSong
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.presentation.ui.screen.home.SongItemContent
import com.example.musicapp.other.viewmodels.SongViewModel
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.youtube.presentation.YoutubeRoute
import com.example.musicapp.youtube.presentation.componenets.SongItemFromYoutube
import com.example.musicapp.youtube.presentation.componenets.Songs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomSheetPlayer(
    state: BottomSheetState,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    BottomSheet(
        state = state,
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
        onDismiss = { },
        collapsedContent = { MiniPlayer(state, mediaControllerManager) }
    ) {
        SongScreenContent(
            state = state,
            mediaControllerManager = mediaControllerManager,
            songViewModel = hiltViewModel<SongViewModel>(),
            navController = navController
        )
    }
}

@UnstableApi
@Preview
@Composable
private fun SongScreenContentPreview() {

    val mediaControllerManager = FakeModule.provideMediaControllerManager()
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
            SongScreenContent(
                playerBottomSheetState,
                mediaControllerManager,
                FakeModule.provideSongViewModel(),
                navController = rememberNavController()
            )
        }
    }


}

@Composable
private fun SongScreenContent(
    state: BottomSheetState,
    mediaControllerManager: MediaControllerManager,
    songViewModel: SongViewModel,
    navController: NavHostController
) {

    val menuState = LocalMenuState.current

    val queue by mediaControllerManager.queue.collectAsState()
    val currentSong by mediaControllerManager.currentSong.collectAsState()
    val playBackState by mediaControllerManager.playBackState.collectAsState()

    var pause by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf("00:00") }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val relatedPage by songViewModel.relatedPage.collectAsState()

    LaunchedEffect(pause) {
        if (!pause) while (true) {
            CoroutineScope(Dispatchers.Main).launch {
                sliderPosition = mediaControllerManager.computePlaybackFraction() ?: 0f
                current = mediaControllerManager.getCurrentTrackPosition().toDurationString()
            }
            delay(100)
        }
    }

    LaunchedEffect(currentSong) {
        currentSong.getArtistId()?.let { songViewModel.getRelated(it) }
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
            currentSong.getThumbnail(),
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TopBarHeight)
            ) {
                CommonIcon(icon = R.drawable.ic_back, onClick = state::collapseSoft)

                Spacer(Modifier.weight(1f))

                CommonIcon(icon = R.drawable.ic_edit, onClick = {
                    menuState.show {
                        Menu(song = currentSong)
                    }
                })
            }

            Text(
                text = currentSong.getTitle(),
                color = White,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE, spacing = MarqueeSpacing.fractionOfContainer(
                        1f / 10f
                    )
                )
            )

            Text(
                text = currentSong.getArtistName(),
                color = White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable {
                    currentSong.getArtistId()?.let {
                        navController.navigate(YoutubeRoute.ARTIST + "/$it") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

            Thumbnail(
                modifier = Modifier
                    .clip(RoundedCornerShape(DefaultCornerSize))
                    .fillMaxWidth()
                    .aspectRatio(1f),
                thumbnailSource = currentSong.getThumbnail()
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
                    text = currentSong.getDurationMillis().toDurationString(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            val iconModifier = Modifier
                .fillMaxHeight()

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {

                CommonIcon(
                    modifier = iconModifier,
                    icon = playBackState.loopMode.resource,
                    onClick = mediaControllerManager::updatePlayListState
                )

                CommonIcon(
                    size = 24.dp,
                    modifier = iconModifier,
                    icon = R.drawable.ic_skip_back,
                    onClick = mediaControllerManager::playPreviousSong
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
                    size = 24.dp,
                    icon = R.drawable.ic_skip_forward,
                    onClick = mediaControllerManager::playNextSong
                )

                CommonIcon(
                    modifier = iconModifier,
                    size = 32.dp,
                    icon = R.drawable.ic_upload,
                    enabled = false
                )

            }

            val list = listOf(
                Pair(R.string.up_next) {
                    menuState.show {
                        QueueView(
                            queue = queue,
                            mediaControllerManager = mediaControllerManager,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f)
                        )
                    }
                },
                Pair(R.string.lyrics) {
                    menuState.show {
                        Lyrics()
                    }
                },
                Pair(R.string.related) {
                    if (queue is Queue.Youtube) {
                        menuState.show {
                            Related(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f),
                                mediaControllerManager = mediaControllerManager,
                                relatedPage = relatedPage
                            )
                        }
                    }
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                list.forEach {
                    Text(
                        text = stringResource(it.first),
                        color = White,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = { it.second() })
                    )
                }
            }
        }

    }
}

@Composable
private fun Lyrics(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.lyrics),
            style = MaterialTheme.typography.titleMedium,
            color = White,
        )
    }

}

@Composable
private fun Related(
    modifier: Modifier = Modifier,
    mediaControllerManager: MediaControllerManager,
    relatedPage: RelatedPage? = null
) {
    Column(
        modifier = modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.related),
            style = MaterialTheme.typography.titleMedium,
            color = White,
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                relatedPage?.songs?.let { songs ->
                    Songs(songs = songs, modifier = Modifier.fillMaxWidth()) { song ->
                        mediaControllerManager.playYoutubeSong(song = song)
                    }
                }
            }
        }
    }
}

@Composable
fun QueueView(
    queue: Queue?,
    mediaControllerManager: MediaControllerManager,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Queue",
            style = MaterialTheme.typography.titleMedium,
            color = White,
        )
        when (queue) {
            is Queue.Youtube -> {
                LazyColumnWithAnimation2(
                    items = queue.songs,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxSize(),
                ) { itemModifier, index, item ->
                    SongItemFromYoutube(
                        modifier = itemModifier.background(Color.Transparent),
                        song = item,
                        onClick = {
                            mediaControllerManager.playAtIndex(index)
                        }
                    )
                }
            }

            is Queue.Other -> {
                LazyColumnWithAnimation2(
                    items = queue.songs,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxSize(),
                ) { itemModifier, index, item ->
                    SongItemContent(song = item, modifier = itemModifier.clickable {
                        mediaControllerManager.playAtIndex(index)
                    })
                }
            }

            null -> return
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MenuPreview() {
    MusicTheme {
        Menu(
            song = FakeModule.provideCurrentSong()
        )
    }
}

@Composable
fun Menu(modifier: Modifier = Modifier, song: CurrentSong) {

    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(topStart = DefaultCornerSize, topEnd = DefaultCornerSize)),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        MyListItem(
            headlineContent = {
                Text(
                    text = song.getTitle(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = White
                )
            },
            leadingContent = {
                Thumbnail(
                    thumbnailSource = song.getThumbnail(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(DefaultCornerSize))
                )
            },
            supportingContent = {
                Text(
                    text = song.getArtistName() + " \u2022 " + song.getDurationMillis().toDurationString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            },
            modifier = Modifier
                .height(80.dp)
                .background(Color.Transparent)
        )

        val pairs = listOf(
            Pair(R.drawable.playlist, R.string.play_next),
            Pair(R.drawable.playlist, R.string.add_to_queue),
            Pair(R.drawable.playlist, R.string.add_to_playlist),
            Pair(R.drawable.playlist, R.string.share),
            Pair(R.drawable.playlist, R.string.dismiss_queue),
            Pair(R.drawable.playlist, R.string.report),
            Pair(R.drawable.playlist, R.string.sleep_timer),
        )

        pairs.forEach {
            MyListItem(
                headlineContent = {
                    Text(
                        text = stringResource(it.second),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = White
                    )
                },
                leadingContent = {
                    CommonIcon(icon = it.first)
                },
                modifier = Modifier
                    .height(40.dp)
                    .background(Color.Transparent)
            )
        }

    }
}