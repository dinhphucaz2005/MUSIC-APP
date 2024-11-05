package com.example.musicapp.domain.model

import androidx.compose.runtime.Immutable
import com.example.musicapp.enums.LoopMode
import com.example.musicapp.enums.PlayerState

@Immutable
data class PlayBackState(
    val playerState: PlayerState,
    val loopMode: LoopMode
) {
    constructor() : this(
        PlayerState.PLAY,
        LoopMode.REPEAT_ALL
    )

    fun updatePlayerState(isPlaying: Boolean) = copy(
        playerState = PlayerState.fromBoolean(isPlaying)
    )

    fun updateLoopMode() = copy(
        loopMode = when (loopMode) {
            LoopMode.SHUFFLE -> LoopMode.REPEAT_ALL
            LoopMode.REPEAT_ALL -> LoopMode.REPEAT_ONE
            LoopMode.REPEAT_ONE -> LoopMode.SHUFFLE
        }
    )
}