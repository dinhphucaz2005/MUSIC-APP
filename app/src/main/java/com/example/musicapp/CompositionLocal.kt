package com.example.musicapp

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.musicapp.core.presentation.components.MenuState
import com.example.musicapp.util.MediaControllerManager

val LocalMediaControllerManager =
    staticCompositionLocalOf<MediaControllerManager?> { error("No PlayerConnection provided") }

val LocalMenuState = compositionLocalOf { MenuState() }
