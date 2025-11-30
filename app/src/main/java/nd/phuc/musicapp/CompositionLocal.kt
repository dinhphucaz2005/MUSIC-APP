package nd.phuc.musicapp

import androidx.compose.runtime.staticCompositionLocalOf
import nd.phuc.music.MediaControllerManager

val LocalMediaControllerManager = staticCompositionLocalOf<MediaControllerManager> {
    error("No MediaControllerManager provided")
}

