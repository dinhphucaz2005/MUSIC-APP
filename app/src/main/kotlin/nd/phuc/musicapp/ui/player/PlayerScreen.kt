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
import androidx.compose.ui.unit.dp
import nd.phuc.musicapp.LocalMediaControllerManager
import org.koin.androidx.compose.koinViewModel
import nd.phuc.musicapp.MediaControllerManager

@Composable
fun MiniPlayer(
    onExpand: () -> Unit,
) {
    val mediaControllerManager = LocalMediaControllerManager.current
    val isPlaying by mediaControllerManager.playerState.collectAsState()
    val currentSong by mediaControllerManager.currentSong.collectAsState()

    if (currentSong == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onExpand() },
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currentSong?.getSongTitle() ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = currentSong?.getSongArtist() ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
            IconButton(onClick = mediaControllerManager::togglePlayPause) {
                Icon(
                    imageVector = if (isPlaying == MediaControllerManager.PlayerState.PLAYING) {
                        Icons.Default.Pause
                    } else {
                        Icons.Default.PlayArrow
                    },
                    contentDescription = "Play/Pause"
                )
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = currentSong?.getSongTitle() ?: "Unknown",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = currentSong?.getSongArtist() ?: "Unknown",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Slider(
            value = if (duration > 0) position.toFloat() / duration else 0f,
            onValueChange = mediaControllerManager::seekToSliderPosition,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatDuration(position))
            Text(text = formatDuration(duration))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = mediaControllerManager::playPreviousSong) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(
                onClick = mediaControllerManager::togglePlayPause,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    if (playerState == MediaControllerManager.PlayerState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size(64.dp)
                )
            }
            IconButton(onClick = mediaControllerManager::playNextSong) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Next",
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onCollapse) {
            Text("Close")
        }
    }
}

private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return "%02d:%02d".format(minutes, seconds)
}
