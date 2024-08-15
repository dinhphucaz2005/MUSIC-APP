package com.example.mymusicapp.enums

import androidx.annotation.DrawableRes
import com.example.mymusicapp.R

enum class PlaylistState(
    @DrawableRes val resource: Int
) {
    SHUFFLE(R.drawable.shuffle),
    REPEAT_ALL(R.drawable.repeat),
    REPEAT_ONE(R.drawable.repeat_one),
}