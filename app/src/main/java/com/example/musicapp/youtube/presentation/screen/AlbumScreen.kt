package com.example.musicapp.youtube.presentation.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innertube.pages.AlbumPage
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.ui.theme.Black
import com.example.musicapp.ui.theme.MyMusicAppTheme
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.youtube.presentation.YoutubeViewModel

@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier, albumId: String, youtubeViewModel: YoutubeViewModel
) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    LaunchedEffect(albumId) { youtubeViewModel.loadAlbum(albumId) }

    val isLoading by youtubeViewModel.isLoading.collectAsState()
    val albumPage by youtubeViewModel.albumPage.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            albumPage?.let {
                AlbumContent(albumPage = it, mediaControllerManager = mediaControllerManager)
            }
        }
    }
}

@Preview
@Composable
private fun ArtistContentPreview() {
    MyMusicAppTheme {
        AlbumContent(FakeModule.provideAlbumPage(), FakeModule.provideMediaControllerManager())
    }
}

@Composable
private fun AlbumContent(albumPage: AlbumPage, mediaControllerManager: MediaControllerManager) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Thumbnail(
                    thumbnailSource = ThumbnailSource.FromUrl(albumPage.album.thumbnail),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = albumPage.album.title,
                    modifier = Modifier.align(Alignment.BottomStart),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Black, fontSize = 56.sp
                    )
                )
            }
        }

        itemsIndexed(items = albumPage.songs, key = { _, item -> item.id }) { index, item ->
            val offsetX = remember { Animatable(100f) }

            LaunchedEffect(item) {
                if (offsetX.value != 0f) {
                    offsetX.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 200)
                    )
                }
            }

            SongItemFromYoutube(
                modifier = Modifier.offset {
                    IntOffset(offsetX.value.toInt(), 0)
                },
                song = item,
                onClick = {
                    TODO()
//                    mediaControllerManager.playYoutubePlaylist(
//                        playlistId = albumPage.album.playlistId,
//                        songs = albumPage.songs,
//                        index = index
//                    )
                }
            )
        }

    }
}
