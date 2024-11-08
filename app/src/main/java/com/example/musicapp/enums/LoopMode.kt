package com.example.musicapp.enums

import androidx.annotation.DrawableRes
import com.example.musicapp.R

enum class LoopMode(
    @DrawableRes val resource: Int
) {
    SHUFFLE(R.drawable.ic_shuffle),
    REPEAT_ALL(R.drawable.ic_repeat),
    REPEAT_ONE(R.drawable.ic_repeat_one),
}