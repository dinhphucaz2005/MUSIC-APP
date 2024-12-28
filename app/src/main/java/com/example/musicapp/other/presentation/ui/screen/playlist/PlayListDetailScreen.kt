package com.example.musicapp.other.presentation.ui.screen.playlist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.core.presentation.components.MyListItem
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.ui.theme.MyMusicAppTheme
import com.example.musicapp.ui.theme.White
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.Playlist
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.other.viewmodels.PlaylistViewModel
import com.example.musicapp.song.SongItemContent

@SuppressLint("UnsafeOptInUsageError")
@Preview
@Composable
fun PlaylistDetailPreview() {
    MyMusicAppTheme {
        PlaylistDetailContent(
            playlist = FakeModule.providePlaylist(),
            viewModel = FakeModule.providePlaylistViewModel(),
            navController = rememberNavController()
        )
    }
}

@Composable
fun PlayListDetail(
    playlistId: Int,
    navController: NavHostController,
    viewModel: PlaylistViewModel,
) {


}

@Composable
private fun PlaylistDetailContent(
    playlist: Playlist,
    viewModel: PlaylistViewModel,
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MyListItem(
            leadingContent = {
                Thumbnail(
                    thumbnailSource = ThumbnailSource.FromBitmap(null),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(DefaultCornerSize))
                )
            },
            headlineContent = {
                Text(
                    text = playlist.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
            },
            supportingContent = {
                Text(
                    text = "${playlist.songs.size} songs",
                    style = MaterialTheme.typography.titleSmall,
                    color = White
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        LazyColumnWithAnimation2(
            items = playlist.songs,
            key = { _, item -> item.id },
            modifier = Modifier
                .fillMaxSize()
        ) { itemModifier, _, song ->
            SongItemContent(
                song = song,
                modifier = itemModifier,
            ) {
                TODO("Not yet implemented")
            }
        }

    }
}