package com.example.musicapp.constants

import androidx.annotation.DrawableRes
import androidx.media3.common.Player
import com.example.musicapp.R

enum class LoopMode(
    @DrawableRes val resource: Int
) {
    SHUFFLE(R.drawable.ic_shuffle),
    REPEAT_ALL(R.drawable.ic_repeat),
    REPEAT_ONE(R.drawable.ic_repeat_one), ;

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