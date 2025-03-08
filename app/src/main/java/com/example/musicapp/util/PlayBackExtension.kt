package com.example.musicapp.util

import com.example.musicapp.R
import com.example.player.model.LoopMode
import com.example.player.model.PlayerState

val PlayerState.resource: Int
    get() {
        return when (this) {
            PlayerState.PAUSE -> R.drawable.ic_pause
            PlayerState.PLAY -> R.drawable.ic_play
        }
    }

val LoopMode.resource: Int
    get() {
        return when (this) {
            LoopMode.SHUFFLE -> R.drawable.ic_shuffle
            LoopMode.REPEAT_ALL -> R.drawable.ic_repeat
            LoopMode.REPEAT_ONE -> R.drawable.ic_repeat_one
        }
    }