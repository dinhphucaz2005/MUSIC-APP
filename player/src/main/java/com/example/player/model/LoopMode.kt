package com.example.player.model

import androidx.media3.common.Player


enum class LoopMode {
    SHUFFLE,
    REPEAT_ALL,
    REPEAT_ONE;

    companion object {
        fun fromInt(repeatMode: Int, shuffleModeEnabled: Boolean): LoopMode {
            return when {
                shuffleModeEnabled -> SHUFFLE
                repeatMode == Player.REPEAT_MODE_ONE -> REPEAT_ONE
                else -> REPEAT_ALL
            }
        }
    }
}