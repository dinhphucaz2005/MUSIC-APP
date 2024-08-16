package com.example.musicapp.enums

import androidx.annotation.DrawableRes
import com.example.musicapp.R

enum class PlaylistState(
    @DrawableRes val resource: Int
) {
    SHUFFLE(R.drawable.shuffle),
    REPEAT_ALL(R.drawable.repeat),
    REPEAT_ONE(R.drawable.repeat_one),
}