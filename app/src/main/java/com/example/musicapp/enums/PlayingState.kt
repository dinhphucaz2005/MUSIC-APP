package com.example.musicapp.enums

import androidx.annotation.DrawableRes
import com.example.musicapp.R

enum class PlayingState(
    @DrawableRes val resource: Int
) {
    TRUE(R.drawable.pause),
    FALSE(R.drawable.play);

    companion object {
        fun fromBoolean(isPlaying: Boolean?): PlayingState = if (isPlaying == true) TRUE else FALSE
    }
}