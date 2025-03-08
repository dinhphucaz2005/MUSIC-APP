package com.example.player.model


enum class PlayerState {
    PAUSE,
    PLAY;

    companion object {
        fun fromBoolean(isPlaying: Boolean?): PlayerState = if (isPlaying == true) PAUSE else PLAY
    }
}