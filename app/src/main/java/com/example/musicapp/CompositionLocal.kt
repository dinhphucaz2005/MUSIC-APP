package com.example.musicapp.youtube

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.musicapp.util.MediaControllerManager

val LocalMediaControllerManager =
    staticCompositionLocalOf<MediaControllerManager?> { error("No PlayerConnection provided") }