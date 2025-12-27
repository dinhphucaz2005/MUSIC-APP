package nd.phuc.musicapp

import androidx.compose.runtime.staticCompositionLocalOf

val LocalMediaControllerManager = staticCompositionLocalOf<MediaControllerManager> {
    error("No MediaControllerManager provided")
}

