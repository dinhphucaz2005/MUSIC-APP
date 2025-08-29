package nd.phuc.musicapp.audio_spectrum

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import nd.phuc.musicapp.LocalMediaControllerManager

@Composable
fun AudioVisualizerScreen() {

    val mediaControllerManager = LocalMediaControllerManager.current

    val audioSessionId by mediaControllerManager?.audioSessionId?.collectAsState() ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        audioSessionId?.let {
            AudioWaveformVisualizer(
                audioSessionId = it
            )
        }
    }
}