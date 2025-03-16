package com.example.musicapp.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.musicapp.R
import com.example.player.model.LoopMode
import com.example.player.model.PlayBackState

data class PlayerIcon(
    @DrawableRes val drawableRes: Int,
    val iconTint: Color? = null
)

val PlayBackState.playPauseIcon: PlayerIcon
    get() {
        return when (this.isPlaying) {
            true -> PlayerIcon(R.drawable.ic_pause)
            false -> PlayerIcon(R.drawable.ic_play)
        }
    }

val PlayBackState.repeatIcon: PlayerIcon
    get() {
        return when (this.loopMode) {
            LoopMode.REPEAT_MODE_ONE -> PlayerIcon(R.drawable.ic_repeat_one)

            LoopMode.REPEAT_MODE_ALL -> PlayerIcon(R.drawable.ic_repeat)

            else -> PlayerIcon(R.drawable.ic_repeat, Color(0xFF9E9E9E))
        }
    }

val PlayBackState.shuffleIcon: PlayerIcon
    get() {
        return when (this.shuffleModeEnable) {
            true -> PlayerIcon(R.drawable.ic_shuffle)
            false -> PlayerIcon(R.drawable.ic_shuffle, Color(0xFF9E9E9E))
        }
    }