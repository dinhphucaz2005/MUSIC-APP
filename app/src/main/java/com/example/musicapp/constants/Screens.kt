package com.example.musicapp.constants

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.example.musicapp.R

@Immutable
sealed class Screens(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
    val route: String
) {
    data object Home : Screens(R.string.home, R.drawable.ic_home, "home")
    data object Playlists : Screens(R.string.playlists, R.drawable.ic_disc, "playlist")
    data object Cloud : Screens(R.string.cloud, R.drawable.ic_cloud, "cloud")
    data object Youtube : Screens(R.string.youtube, R.drawable.ic_youtube, "youtube")
    data object Setting : Screens(R.string.setting, R.drawable.ic_setting, "setting")
    data object AudioVisualizer: Screens(R.string.audio_visualizer, R.drawable.audio, "audio_visualizer")

    companion object {
        val MainScreens = listOf(Home, Playlists, Cloud, Youtube, Setting, AudioVisualizer)
    }
}
