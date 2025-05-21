package com.example.musicapp

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.musicapp.core.presentation.components.MenuState
import com.example.musicapp.music.domain.model.CurrentSong
import com.example.musicapp.music.domain.model.PlayBackState
import com.example.musicapp.music.domain.model.Queue
import com.example.musicapp.music.domain.model.Song
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.util.MediaControllerManagerImpl
import com.example.musicapp.util.UninitializedMediaControllerManager
import kotlinx.coroutines.flow.StateFlow

val LocalMediaControllerManager =
    staticCompositionLocalOf<MediaControllerManager> { UninitializedMediaControllerManager() }

val LocalMenuState = compositionLocalOf { MenuState() }
