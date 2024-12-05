package com.example.musicapp.constants

import androidx.annotation.DrawableRes
import com.example.musicapp.R

enum class PlayerState(
    @DrawableRes val resource: Int
) {
    PAUSE(R.drawable.ic_pause),
    PLAY(R.drawable.ic_play);

    companion object {
        fun fromBoolean(isPlaying: Boolean?): PlayerState = if (isPlaying == true) PAUSE else PLAY
    }
}