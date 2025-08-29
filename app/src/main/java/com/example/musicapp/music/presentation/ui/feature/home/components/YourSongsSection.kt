package com.example.musicapp.music.presentation.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.LocalMenuState
import com.example.musicapp.core.presentation.components.SongItemContent
import com.example.musicapp.core.presentation.previews.ExtendDevicePreviews
import com.example.musicapp.di.FakeModule
import com.example.musicapp.music.domain.model.Song
import com.example.musicapp.ui.theme.MyMusicAppTheme

@ExtendDevicePreviews
@Composable
private fun YourSongsSectionPreview() {
    MyMusicAppTheme {
        Box(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
        ) {
            YourSongsSection(
                songs = listOf(
                    FakeModule.localSong,
                    FakeModule.localSong,
                    FakeModule.localSong,
                    FakeModule.localSong,
                    FakeModule.localSong,
                    FakeModule.localSong,
                ),
            )
        }
    }
}


@Composable
fun YourSongsSection(
    songs: List<Song>,
) {

    val mediaControllerManager = LocalMediaControllerManager.current
    val menuState = LocalMenuState.current

    Column {
        songs.take(10).forEachIndexed { index, song ->
            SongItemContent(
                song = song,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                onSongClick = {
                    mediaControllerManager.playQueue(
                        songs = songs,
                        index = index,
                        id = "songs.lastReloadMillis.toString()"
                    )
                },
                onMoreChoice = {
                    menuState.show {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            )
        }
    }
}