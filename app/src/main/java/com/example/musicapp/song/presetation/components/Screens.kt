package com.example.musicapp.song.presetation.components

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
    data object PlaylistHome : Screens(R.string.playlist_home, R.drawable.ic_disc, "playlist_home")
    data object PlaylistDetail : Screens(R.string.playlist_detail, R.drawable.ic_disc, "playlist_detail")
    data object PlaylistEdit : Screens(R.string.playlist_edit, R.drawable.ic_disc, "playlist_edit")

    data object Cloud : Screens(R.string.cloud, R.drawable.ic_cloud, "cloud")

    data object Youtube : Screens(R.string.youtube, R.drawable.ic_youtube, "youtube")
    data object YoutubeSearch : Screens(R.string.youtube_search, R.drawable.ic_youtube, "youtube_home")
    data object YoutubeHome : Screens(R.string.youtube_home, R.drawable.ic_youtube, "youtube_search")
    data object YoutubePlaylistDetail: Screens(R.string.youtube_playlist_detail, R.drawable.ic_youtube, "youtube_playlist_detail")

    data object Setting : Screens(R.string.setting, R.drawable.ic_setting, "setting")

    companion object {
        val MainScreens = listOf(Home, Playlists, Cloud, Youtube, Setting)
    }
}
