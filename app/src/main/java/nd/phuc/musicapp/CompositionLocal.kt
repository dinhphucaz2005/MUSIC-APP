package nd.phuc.musicapp

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import nd.phuc.musicapp.core.presentation.components.MenuState
import nd.phuc.musicapp.util.MediaControllerManager

val LocalMediaControllerManager =
    staticCompositionLocalOf<MediaControllerManager> { _root_ide_package_.nd.phuc.musicapp.util.UninitializedMediaControllerManager() }

val LocalMenuState = compositionLocalOf { MenuState() }
