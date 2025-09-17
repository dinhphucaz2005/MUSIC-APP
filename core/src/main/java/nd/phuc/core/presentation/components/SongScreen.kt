package nd.phuc.core.presentation.components

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.media3.common.util.UnstableApi
//import nd.phuc.musicapp.LocalMediaControllerManager
//import nd.phuc.musicapp.LocalMenuState
//import nd.phuc.musicapp.R
//import nd.phuc.musicapp.constants.DefaultCornerSize
//import nd.phuc.musicapp.constants.MiniPlayerHeight
//import nd.phuc.musicapp.constants.NavigationBarHeight
//import nd.phuc.musicapp.constants.PlayerState
//import nd.phuc.musicapp.constants.TopBarHeight
//import nd.phuc.musicapp.di.FakeModule
//import nd.phuc.musicapp.extension.toDurationString
//import nd.phuc.musicapp.extension.withMainContext
//import nd.phuc.core.model.Queue
//import nd.phuc.core.model.Song
//import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
//import nd.phuc.musicapp.util.MediaControllerManager
//import kotlinx.coroutines.delay
//
//@Composable
//fun BottomSheetPlayer(
//    state: BottomSheetState,
//    modifier: Modifier = Modifier,
//) {
//
//    val mediaControllerManager = LocalMediaControllerManager.current
//
//    BottomSheet(
//        state = state,
//        modifier = modifier,
//        backgroundColor = MaterialTheme.colorScheme.background,
//        onDismiss = { },
//        collapsedContent = { MiniPlayer(state) }
//    ) {
//        SongScreenContent(
//            state = state,
//            mediaControllerManager = mediaControllerManager
//        )
//    }
//}
//
//@SuppressLint("UnusedBoxWithConstraintsScope")
//@UnstableApi
//@Preview
//@Composable
//private fun SongScreenContentPreview() {
//
//    val mediaControllerManager = FakeModule.mediaControllerManager
//    val shouldShowNavigationBar = false
//
//    val density = LocalDensity.current
//    val windowsInsets = WindowInsets.systemBars
//    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }
//
//
//    BoxWithConstraints(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        val playerBottomSheetState = rememberBottomSheetState(
//            dismissedBound = 0.dp,
//            collapsedBound = bottomInset + (if (shouldShowNavigationBar) NavigationBarHeight else 0.dp) + MiniPlayerHeight,
//            expandedBound = maxHeight,
//        )
//        MyMusicAppTheme {
//            SongScreenContent(
//                state = playerBottomSheetState,
//                mediaControllerManager = mediaControllerManager
//            )
//        }
//    }
//
//
//}
//
//@Composable
//private fun SongScreenContent(
//    state: BottomSheetState,
//    mediaControllerManager: MediaControllerManager
//) {
//
//    val menuState = LocalMenuState.current
//
//    val queue by mediaControllerManager.queue.collectAsState()
//    val currentSong by mediaControllerManager.currentSong.collectAsState()
//    val playBackState by mediaControllerManager.playBackState.collectAsState()
//
//    var pause by remember { mutableStateOf(false) }
//    var current by remember { mutableStateOf("00:00") }
//    var sliderPosition by remember { mutableFloatStateOf(0f) }
//
////    val relatedPage by songViewModel.relatedPage.collectAsState()
//
//    LaunchedEffect(pause) {
//        if (!pause) while (true) {
//            withMainContext {
//                sliderPosition = mediaControllerManager.computePlaybackFraction() ?: 0f
//                current = mediaControllerManager.getCurrentTrackPosition().toDurationString()
//            }
//            delay(100)
//        }
//    }
//
//
//    Box(
//        Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//    ) {
//        Thumbnail(
//            Modifier
//                .fillMaxSize()
//                .blur(50.dp),
//            currentSong.data.getThumbnail(),
//            contentScale = ContentScale.FillHeight
//        )
//        Box(
//            Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(0.5f))
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(TopBarHeight)
//            ) {
//                CommonIcon(icon = R.drawable.ic_back, onClick = state::collapseSoft)
//
//                Spacer(Modifier.weight(1f))
//
//                CommonIcon(
//                    icon = R.drawable.ic_download,
//                    onClick = mediaControllerManager::downLoadCurrentSong
//                )
//            }
//
//            Text(
//                text = currentSong.data.getSongTitle(),
//                color = MaterialTheme.colorScheme.onBackground,
//                maxLines = 1,
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.basicMarquee(
//                    iterations = Int.MAX_VALUE, spacing = MarqueeSpacing.fractionOfContainer(
//                        1f / 10f
//                    )
//                )
//            )
//
//            Text(
//                text = currentSong.data.getSongArtist(),
//                color = MaterialTheme.colorScheme.onBackground,
//                style = MaterialTheme.typography.titleMedium,
//            )
//
//            AnimatedBorder(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f)
//            ) {
//                Thumbnail(
//                    modifier = it,
//                    thumbnailSource = currentSong.data.getThumbnail(),
//                    contentScale = ContentScale.Crop
//                )
//            }
//
//
//            CustomSlider(
//                modifier = Modifier.fillMaxWidth(),
//                value = sliderPosition,
//                onValueChange = {
//                    sliderPosition = it
//                    pause = true
//                },
//                onValueChangeFinished = {
//                    mediaControllerManager.seekToSliderPosition(sliderPosition)
//                    pause = false
//                },
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                Text(
//                    text = current,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    style = MaterialTheme.typography.labelSmall
//                )
//                Spacer(Modifier.weight(1f))
//                Text(
//                    text = currentSong.data.getDuration(),
//                    color = MaterialTheme.colorScheme.onBackground,
//                    style = MaterialTheme.typography.labelSmall
//                )
//            }
//
//            val iconModifier = Modifier
//                .fillMaxHeight()
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(60.dp)
//            ) {
//
//                CommonIcon(
//                    modifier = iconModifier,
//                    icon = playBackState.loopMode.resource,
//                    onClick = mediaControllerManager::updatePlayListState
//                )
//
//                CommonIcon(
//                    size = 24.dp,
//                    modifier = iconModifier,
//                    icon = R.drawable.ic_skip_back,
//                    onClick = mediaControllerManager::playPreviousSong
//                )
//
//                val cornerSize by animateIntAsState(
//                    targetValue = if (playBackState.playerState == PlayerState.PLAY) 50 else 12,
//                    animationSpec = tween(durationMillis = 100), label = ""
//                )
//
//                Box(
//                    modifier = Modifier
//                        .clickable(onClick = mediaControllerManager::togglePlayPause)
//                        .clip(RoundedCornerShape(cornerSize))
//                        .background(MaterialTheme.colorScheme.error)
//                        .fillMaxHeight()
//                        .aspectRatio(1f),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Image(
//                        painter = painterResource(playBackState.playerState.resource),
//                        contentDescription = null
//                    )
//                }
//
//
//                CommonIcon(
//                    modifier = iconModifier,
//                    size = 24.dp,
//                    icon = R.drawable.ic_skip_forward,
//                    onClick = mediaControllerManager::playNextSong
//                )
//
//                CommonIcon(
//                    modifier = iconModifier,
//                    size = 32.dp,
//                    icon = R.drawable.ic_upload,
//                    enabled = false
//                )
//
//            }
//
//            val list = listOf(
//                Pair(R.string.up_next) {
//                    menuState.show {
//                        queue?.let {
//                            QueueView(
//                                queue = it,
//                                mediaControllerManager = mediaControllerManager,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .fillMaxHeight(0.6f)
//                            )
//                        }
//                    }
//                },
//                Pair(R.string.lyrics) {
//                    menuState.show {
//                        Lyrics()
//                    }
//                },
//                Pair(R.string.related) {
//                    menuState.show {
////                        Related(
////                            mediaControllerManager = mediaControllerManager,
////                            relatedPage = relatedPage
////                        )
//                    }
//                },
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                list.forEach {
//                    Text(
//                        text = stringResource(it.first),
//                        color = MaterialTheme.colorScheme.onBackground,
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.labelLarge,
//                        modifier = Modifier
//                            .weight(1f)
//                            .clickable(onClick = { it.second() })
//                    )
//                }
//            }
//        }
//
//    }
//}
//
//@Composable
//private fun Lyrics(modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier
//            .padding(12.dp),
//        verticalArrangement = Arrangement.spacedBy(6.dp)
//    ) {
//        Text(
//            text = stringResource(R.string.lyrics),
//            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.primary,
//        )
//    }
//
//}
//
//@Composable
//private fun Related(
//    modifier: Modifier = Modifier,
//    mediaControllerManager: MediaControllerManager,
//    relatedPage: RelatedPage? = null
//) {
//    Column(
//        modifier = modifier
//            .padding(12.dp),
//        verticalArrangement = Arrangement.spacedBy(6.dp)
//    ) {
//        Text(
//            text = stringResource(R.string.related),
//            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.primary,
//        )
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            verticalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//            item {
//                relatedPage?.songs?.let { songs ->
//                    Songs(
//                        songs = songs,
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = mediaControllerManager::playYoutubeSong
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun QueueView(
//    queue: Queue,
//    mediaControllerManager: MediaControllerManager,
//    modifier: Modifier = Modifier,
//) {
//    val menu = LocalMenuState.current
//    val currentIndex = mediaControllerManager.getCurrentMediaIndex() ?: 0
//    val state = rememberLazyListState()
//
//    LaunchedEffect(currentIndex) {
//        state.scrollToItem(currentIndex)
//    }
//
//
//    Column(
//        modifier = modifier
//            .padding(12.dp),
//        verticalArrangement = Arrangement.spacedBy(6.dp)
//    ) {
//
//        Text(
//            text = "Queue",
//            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.primary,
//        )
//
//        LazyColumnWithAnimation2(
//            modifier = modifier
//                .fillMaxWidth()
//                .weight(1f),
//            items = queue.songs,
//            state = state,
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            key = { _, item -> item.id.toString() }
//        ) { itemModifier, index, song ->
//            SongItemContent(
//                song = song,
//                modifier = itemModifier,
//                onSongClick = { mediaControllerManager.playAtIndex(index) },
//                onMoreChoice = { menu.show { Menu(song = song) } }
//            )
//        }
//
//    }
//}
//
//
//@Composable
//fun Menu(modifier: Modifier = Modifier, song: Song) {
//
//    Column(
//        modifier = modifier
//            .padding(12.dp)
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .clip(RoundedCornerShape(topStart = DefaultCornerSize, topEnd = DefaultCornerSize)),
//        verticalArrangement = Arrangement.spacedBy(6.dp)
//    ) {
//
//        MyListItem(
//            headlineContent = {
//                Text(
//                    text = song.getSongTitle(),
//                    style = MaterialTheme.typography.titleMedium,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            },
//            leadingContent = {
//                Thumbnail(
//                    thumbnailSource = song.getThumbnail(),
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .aspectRatio(1f)
//                        .clip(RoundedCornerShape(DefaultCornerSize))
//                )
//            },
//            supportingContent = {
//                Text(
//                    text = song.getSongArtist() + " \u2022 " + song.getDuration(),
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.tertiary,
//                    modifier = Modifier.padding(top = 4.dp)
//                )
//            },
//            modifier = Modifier
//                .height(80.dp)
//                .background(Color.Transparent)
//        )
//
//        val pairs = listOf(
//            Pair(R.drawable.playlist, R.string.play_next),
//            Pair(R.drawable.playlist, R.string.add_to_queue),
//            Pair(R.drawable.playlist, R.string.add_to_playlist),
//            Pair(R.drawable.playlist, R.string.share),
//            Pair(R.drawable.playlist, R.string.dismiss_queue),
//            Pair(R.drawable.playlist, R.string.report),
//            Pair(R.drawable.playlist, R.string.sleep_timer),
//        )
//
//        pairs.forEach {
//            MyListItem(
//                headlineContent = {
//                    Text(
//                        text = stringResource(it.second),
//                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                },
//                leadingContent = {
//                    CommonIcon(icon = it.first)
//                },
//                modifier = Modifier
//                    .height(40.dp)
//                    .background(Color.Transparent)
//            )
//        }
//
//    }
//}