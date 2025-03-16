package com.example.player.model

import androidx.compose.runtime.Immutable
import androidx.media3.session.MediaController


@Immutable
data class PlayBackState(
    val isPlaying: Boolean,
    val loopMode: LoopMode,
    val shuffleModeEnable: Boolean
) {
    fun updatePlayerState(playing: Boolean): PlayBackState {
        return copy(isPlaying = playing)
    }

    companion object {
        fun fromController(controller: MediaController): PlayBackState {
            return PlayBackState(
                isPlaying = controller.isPlaying,
                loopMode = when (controller.repeatMode) {
                    MediaController.REPEAT_MODE_ALL -> LoopMode.REPEAT_MODE_ALL
                    MediaController.REPEAT_MODE_ONE -> LoopMode.REPEAT_MODE_ONE
                    else -> LoopMode.REPEAT_MODE_OFF
                },
                shuffleModeEnable = controller.shuffleModeEnabled
            )
        }

        fun initial(): PlayBackState = PlayBackState(
            isPlaying = false,
            loopMode = LoopMode.REPEAT_MODE_OFF,
            shuffleModeEnable = false
        )

        fun fromController(
            isPlaying: Boolean,
            repeatMode: Int,
            shuffleModeEnabled: Boolean
        ): PlayBackState {
            return PlayBackState(
                isPlaying = isPlaying,
                loopMode = when (repeatMode) {
                    MediaController.REPEAT_MODE_ALL -> LoopMode.REPEAT_MODE_ALL
                    MediaController.REPEAT_MODE_ONE -> LoopMode.REPEAT_MODE_ONE
                    else -> LoopMode.REPEAT_MODE_OFF
                },
                shuffleModeEnable = shuffleModeEnabled
            )
        }
    }
}