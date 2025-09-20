package nd.phuc.musicapp.music

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.sample
import nd.phuc.core.extension.toDurationString
import nd.phuc.core.domain.model.DefaultCornerSize
import nd.phuc.core.domain.model.MiniPlayerHeight
import nd.phuc.core.domain.model.TopBarHeight
import nd.phuc.core.presentation.components.AnimatedBorder
import nd.phuc.core.presentation.components.BottomSheet
import nd.phuc.core.presentation.components.BottomSheetState
import nd.phuc.core.presentation.components.CommonIcon
import nd.phuc.core.presentation.components.CustomSlider
import nd.phuc.core.presentation.components.Thumbnail
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.R
import nd.phuc.core.helper.MediaControllerManager


private val MediaControllerManager.PlayerState.resource: Int
    get() = when (this) {
        MediaControllerManager.PlayerState.PLAYING -> R.drawable.ic_pause
        MediaControllerManager.PlayerState.PAUSED, MediaControllerManager.PlayerState.STOPPED -> R.drawable.ic_play
    }
private val MediaControllerManager.PlayerState.isPlaying: Boolean
    get() = this == MediaControllerManager.PlayerState.PLAYING
private val MediaControllerManager.RepeatState.resource: Int
    get() = when (this) {
        MediaControllerManager.RepeatState.OFF -> R.drawable.ic_repeat_off
        MediaControllerManager.RepeatState.ALL -> R.drawable.ic_repeat
        MediaControllerManager.RepeatState.ONE -> R.drawable.ic_repeat_one
    }

private val MediaControllerManager.ShuffleState.resource: Int
    get() = when (this) {
        MediaControllerManager.ShuffleState.OFF -> R.drawable.ic_shuffle_off
        MediaControllerManager.ShuffleState.ON -> R.drawable.ic_shuffle
    }

@Composable
fun BottomSheetPlayer(
    state: BottomSheetState,
    modifier: Modifier = Modifier,
) {

    val mediaControllerManager = LocalMediaControllerManager.current

    BottomSheet(
        state = state,
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
        onDismiss = { },
        collapsedContent = { MiniPlayer(state) }
    ) {
        SongScreenContent(
            state = state,
            mediaControllerManager = mediaControllerManager
        )
    }
}


@SuppressLint("FlowOperatorInvokedInComposition")
@OptIn(FlowPreview::class)
@Composable
private fun SongScreenContent(
    state: BottomSheetState,
    mediaControllerManager: MediaControllerManager,
) {

    val currentSong by mediaControllerManager.currentSong.collectAsStateWithLifecycle()
    val playerState by mediaControllerManager.playerState.collectAsStateWithLifecycle(
        MediaControllerManager.PlayerState.PLAYING
    )

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
                CommonIcon(
                    icon = R.drawable.ic_back,
                    onClick = state::collapseSoft
                )

                Spacer(Modifier.weight(1f))
            }

            Text(
                text = currentSong.getSongTitle(),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE, spacing = MarqueeSpacing.fractionOfContainer(
                        1f / 10f
                    )
                )
            )

            Text(
                text = currentSong.getSongArtist(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )

            AnimatedBorder(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Thumbnail(
                    thumbnailSource = currentSong.getThumbnail(),
                    contentScale = ContentScale.Crop
                )
            }

            PlayerControls(
                mediaControllerManager = mediaControllerManager,
                playerState = playerState
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
        }

    }
}

@OptIn(FlowPreview::class)
@Composable
fun PlayerControls(
    mediaControllerManager: MediaControllerManager,
    playerState: MediaControllerManager.PlayerState,
) {
    var isSeeking by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    val shuffleState by mediaControllerManager.shuffleState.collectAsStateWithLifecycle()
    val repeatState by mediaControllerManager.repeatState.collectAsStateWithLifecycle()

    val position by remember(playerState, isSeeking) {
        if (playerState == MediaControllerManager.PlayerState.PLAYING && !isSeeking) {
            mediaControllerManager.position.sample(500)
        } else {
            flowOf(0L)
        }
    }.collectAsStateWithLifecycle(0L)
    val duration by mediaControllerManager.duration.collectAsState()

    CustomSlider(
        modifier = Modifier.fillMaxWidth(),
        value = if (isSeeking) progress else if (duration == 0L) 0f else position / duration.toFloat(),
        onValueChange = {
            isSeeking = true
            progress = it
        },
        onValueChangeFinished = {
            isSeeking = false
            mediaControllerManager.seekToSliderPosition(progress)
        },
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = position.toDurationString(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = duration.toDurationString(),
            color = MaterialTheme.colorScheme.onBackground,
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
            icon = repeatState.resource,
            onClick = mediaControllerManager::toggleRepeat,
        )

        CommonIcon(
            size = 24.dp,
            modifier = iconModifier,
            icon = R.drawable.ic_skip_back,
            onClick = mediaControllerManager::playPreviousSong
        )

        val cornerSize by animateIntAsState(
            targetValue = if (playerState.isPlaying) 50 else 12,
            animationSpec = tween(durationMillis = 100), label = ""
        )

        Box(
            modifier = Modifier
                .clickable(onClick = mediaControllerManager::togglePlayPause)
                .clip(RoundedCornerShape(cornerSize))
                .background(MaterialTheme.colorScheme.error)
                .fillMaxHeight()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(playerState.resource),
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
            size = 24.dp,
            icon = shuffleState.resource,
            onClick = mediaControllerManager::toggleShuffle
        )


    }
}

@Composable
fun MiniPlayer(
    state: BottomSheetState,
) {
    val mediaControllerManager = LocalMediaControllerManager.current

    val currentSong by mediaControllerManager.currentSong.collectAsStateWithLifecycle()
    val playerState by mediaControllerManager.playerState.collectAsStateWithLifecycle()


    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .height(MiniPlayerHeight)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(8.dp)
            .clickable(onClick = state::expandSoft),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Thumbnail(
            Modifier
                .clip(RoundedCornerShape(DefaultCornerSize))
                .fillMaxHeight()
                .aspectRatio(1f),
            thumbnailSource = currentSong.getThumbnail()
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = currentSong.getSongTitle(),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        spacing = MarqueeSpacing.fractionOfContainer(1f / 10f)
                    ),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp, color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = currentSong.getSongArtist(),
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp, color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        IconButton(onClick = mediaControllerManager::toggleLikedCurrentSong) {
            Icon(
                imageVector = if (currentSong.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null, tint = MaterialTheme.colorScheme.primary
            )
        }

        Icon(
            painter = painterResource(playerState.resource),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable {
                    mediaControllerManager.togglePlayPause()
                }, tint = MaterialTheme.colorScheme.primary
        )

        IconButton(onClick = mediaControllerManager::playNextSong) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null, tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}