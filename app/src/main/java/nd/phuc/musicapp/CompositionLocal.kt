package nd.phuc.musicapp

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import nd.phuc.musicapp.core.presentation.components.MenuState
import nd.phuc.musicapp.util.MediaControllerManager
import nd.phuc.musicapp.util.UninitializedMediaControllerManager

val LocalMediaControllerManager =
    staticCompositionLocalOf<MediaControllerManager> { UninitializedMediaControllerManager() }

val LocalMenuState = compositionLocalOf { MenuState() }
