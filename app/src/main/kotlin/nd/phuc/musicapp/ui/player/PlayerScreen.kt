package nd.phuc.musicapp.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.MediaControllerManager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
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
        onPlayPause = mediaControllerManager::togglePlayPause
    )
}

@Composable
fun MiniPlayerContent(
    isPlaying: Boolean,
    currentSong: nd.phuc.musicapp.model.Song?,
    position: Long,
    duration: Long,
    onExpand: () -> Unit,
    onPlayPause: () -> Unit,
) {
    if (currentSong == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onExpand() },
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp
    ) {
        Column {
            // Thin progress bar at the top
            if (duration > 0) {
                LinearProgressIndicator(
                    progress = { position.toFloat() / duration },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
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
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentSong.getSongTitle(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = currentSong.getSongArtist(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1
                    )
                }
                IconButton(onClick = onPlayPause) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause"
                    )
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

    FullPlayerContent(
        playerState = playerState,
        currentSong = currentSong,
        position = position,
        duration = duration,
        onCollapse = onCollapse,
        onPlayPause = mediaControllerManager::togglePlayPause,
        onPrevious = mediaControllerManager::playPreviousSong,
        onNext = mediaControllerManager::playNextSong,
        onSeek = mediaControllerManager::seekToSliderPosition
    )
}

@Composable
fun FullPlayerContent(
    playerState: MediaControllerManager.PlayerState,
    currentSong: nd.phuc.musicapp.model.Song?,
    position: Long,
    duration: Long,
    onCollapse: () -> Unit,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            onClick = onCollapse, modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                Icons.Default.SkipPrevious,
                contentDescription = "Back",
                modifier = Modifier.size(32.dp)
            ) // Using SkipPrevious as a back icon for now
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
                .clip(RoundedCornerShape(16.dp))
                .shadow(8.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = currentSong?.getSongTitle() ?: "Unknown",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentSong?.getSongArtist() ?: "Unknown",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Slider(
            value = if (duration > 0) position.toFloat() / duration else 0f,
            onValueChange = onSeek,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatDuration(position), style = MaterialTheme.typography.bodySmall)
            Text(text = formatDuration(duration), style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevious) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(
                onClick = onPlayPause, modifier = Modifier.size(80.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(40.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        if (playerState == MediaControllerManager.PlayerState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            IconButton(onClick = onNext) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Next",
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniPlayerPreview() {
    val mockSong = LocalSong(
        "Preview Song", "Preview Artist", "path", ThumbnailSource.None, 300000, false
    )
    MiniPlayerContent(
        isPlaying = true,
        currentSong = mockSong,
        position = 120000,
        duration = 300000,
        onExpand = {},
        onPlayPause = {})
}

@Preview(showBackground = true)
@Composable
private fun FullPlayerPreview() {
    val mockSong = LocalSong(
        "Preview Song", "Preview Artist", "path", ThumbnailSource.None, 300000, false
    )
    FullPlayerContent(
        playerState = MediaControllerManager.PlayerState.PLAYING,
        currentSong = mockSong,
        position = 120000,
        duration = 300000,
        onCollapse = {},
        onPlayPause = {},
        onPrevious = {},
        onNext = {},
        onSeek = {})
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return "%02d:%02d".format(minutes, seconds)
}
