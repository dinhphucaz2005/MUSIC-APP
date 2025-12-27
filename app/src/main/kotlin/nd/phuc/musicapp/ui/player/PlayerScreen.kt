package nd.phuc.musicapp.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.basicMarquee
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.MediaControllerManager
import nd.phuc.musicapp.model.LocalSong
import nd.phuc.musicapp.model.ThumbnailSource

@Composable
fun MiniPlayer(
    onExpand: () -> Unit,
) {
    val mediaControllerManager = LocalMediaControllerManager.current
    val playerState by mediaControllerManager.playerState.collectAsState()
    val currentSong by mediaControllerManager.currentSong.collectAsState()
    val position by mediaControllerManager.position.collectAsState()
    val duration by mediaControllerManager.duration.collectAsState()

    MiniPlayerContent(
        isPlaying = playerState == MediaControllerManager.PlayerState.PLAYING,
        currentSong = currentSong,
        position = position,
        duration = duration,
        onExpand = onExpand,
        onPlayPause = mediaControllerManager::togglePlayPause,
        onNext = mediaControllerManager::playNextSong
    )
}

@Composable
private fun MiniPlayerContent(
    isPlaying: Boolean,
    currentSong: nd.phuc.musicapp.model.Song?,
    position: Long,
    duration: Long,
    onExpand: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
) {
    if (currentSong == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onExpand() },
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
        tonalElevation = 4.dp
    ) {
        Column {
            if (duration > 0) {
                LinearProgressIndicator(
                    progress = { position.toFloat() / duration },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.Transparent
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = when (val source = currentSong.getThumbnail()) {
                        is ThumbnailSource.FromUrl -> source.url
                        is ThumbnailSource.FromByteArray -> source.byteArray
                        is ThumbnailSource.FilePath -> source.path
                        else -> null
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentSong.getSongTitle(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                    Text(
                        text = currentSong.getSongArtist(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                }
                Row {
                    IconButton(onClick = onPlayPause) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = "Play/Pause"
                        )
                    }
                    IconButton(onClick = onNext) {
                        Icon(
                            imageVector = Icons.Default.SkipNext, contentDescription = "Next"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FullPlayer(
    onCollapse: () -> Unit,
) {
    val mediaControllerManager = LocalMediaControllerManager.current
    val playerState by mediaControllerManager.playerState.collectAsState()
    val currentSong by mediaControllerManager.currentSong.collectAsState()
    val position by mediaControllerManager.position.collectAsState()
    val duration by mediaControllerManager.duration.collectAsState()
    val shuffleState by mediaControllerManager.shuffleState.collectAsState()
    val repeatState by mediaControllerManager.repeatState.collectAsState()

    FullPlayerContent(
        playerState = playerState,
        currentSong = currentSong,
        position = position,
        duration = duration,
        shuffleState = shuffleState,
        repeatState = repeatState,
        onCollapse = onCollapse,
        onPlayPause = mediaControllerManager::togglePlayPause,
        onPrevious = mediaControllerManager::playPreviousSong,
        onNext = mediaControllerManager::playNextSong,
        onSeek = mediaControllerManager::seekToSliderPosition,
        onToggleShuffle = mediaControllerManager::toggleShuffle,
        onToggleRepeat = mediaControllerManager::toggleRepeat,
        onToggleLike = { /* TODO: Implement like logic */ })
}

@Composable
private fun FullPlayerContent(
    playerState: MediaControllerManager.PlayerState,
    currentSong: nd.phuc.musicapp.model.Song?,
    position: Long,
    duration: Long,
    shuffleState: MediaControllerManager.ShuffleState,
    repeatState: MediaControllerManager.RepeatState,
    onCollapse: () -> Unit,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Float) -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleLike: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Premium Background: Darkened Album Art
        AsyncImage(
            model = when (val source = currentSong?.getThumbnail()) {
                is ThumbnailSource.FromUrl -> source.url
                is ThumbnailSource.FromByteArray -> source.byteArray
                is ThumbnailSource.FilePath -> source.path
                else -> null
            },
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f), Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                onClick = onCollapse, modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Collapse",
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AsyncImage(
                model = when (val source = currentSong?.getThumbnail()) {
                    is ThumbnailSource.FromUrl -> source.url
                    is ThumbnailSource.FromByteArray -> source.byteArray
                    is ThumbnailSource.FilePath -> source.path
                    else -> null
                },
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(16.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentSong?.getSongTitle() ?: "Unknown",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                    Text(
                        text = currentSong?.getSongArtist() ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                }
                IconButton(onClick = onToggleLike) {
                    Icon(
                        imageVector = if (currentSong?.isLiked == true) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (currentSong?.isLiked == true) Color.Red else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Slider(
                value = if (duration > 0) position.toFloat() / duration else 0f,
                onValueChange = onSeek,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDuration(position),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = formatDuration(duration),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggleShuffle) {
                    Icon(
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (shuffleState == MediaControllerManager.ShuffleState.ON) MaterialTheme.colorScheme.primary else Color.White
                    )
                }
                IconButton(onClick = onPrevious) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = onPlayPause, modifier = Modifier.size(80.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(40.dp),
                        color = Color.White,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            imageVector = if (playerState == MediaControllerManager.PlayerState.PLAYING) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = "Play/Pause",
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            tint = Color.Black
                        )
                    }
                }
                IconButton(onClick = onNext) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
                IconButton(onClick = onToggleRepeat) {
                    Icon(
                        imageVector = when (repeatState) {
                            MediaControllerManager.RepeatState.ONE -> Icons.Rounded.RepeatOne
                            MediaControllerManager.RepeatState.ALL -> Icons.Rounded.Repeat
                            else -> Icons.Rounded.Repeat
                        },
                        contentDescription = "Repeat",
                        tint = if (repeatState != MediaControllerManager.RepeatState.OFF) MaterialTheme.colorScheme.primary else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniPlayerPreview() {
    val mockSong = LocalSong(
        "Preview Song with a very long title that should marquee",
        "Preview Artist",
        "path",
        ThumbnailSource.None,
        300000,
        false
    )
    MiniPlayerContent(
        isPlaying = true,
        currentSong = mockSong,
        position = 120000,
        duration = 300000,
        onExpand = {},
        onPlayPause = {},
        onNext = {})
}

@Preview(showBackground = true)
@Composable
private fun FullPlayerPreview() {
    val mockSong = LocalSong(
        "Preview Song with a very long title that should marquee",
        "Preview Artist",
        "path",
        ThumbnailSource.None,
        300000,
        false
    )
    FullPlayerContent(
        playerState = MediaControllerManager.PlayerState.PLAYING,
        currentSong = mockSong,
        position = 120000,
        duration = 300000,
        shuffleState = MediaControllerManager.ShuffleState.OFF,
        repeatState = MediaControllerManager.RepeatState.ALL,
        onCollapse = {},
        onPlayPause = {},
        onPrevious = {},
        onNext = {},
        onSeek = {},
        onToggleShuffle = {},
        onToggleRepeat = {},
        onToggleLike = {})
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return "%02d:%02d".format(minutes, seconds)
}
