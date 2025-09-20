package nd.phuc.musicapp

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import nd.phuc.core.presentation.components.MenuState
import nd.phuc.core.helper.MediaControllerManager

val LocalMediaControllerManager = staticCompositionLocalOf<MediaControllerManager> {
    error("No MediaControllerManager provided")
}

val LocalMenuState = compositionLocalOf { MenuState() }
