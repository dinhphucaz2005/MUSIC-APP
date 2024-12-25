package com.example.musicapp.youtube.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.innertube.models.AlbumItem
import com.example.innertube.models.ArtistItem
import com.example.innertube.models.PlaylistItem
import com.example.innertube.models.SongItem
import com.example.innertube.pages.ArtistPage
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.core.presentation.theme.Black
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.core.presentation.theme.White
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.youtube.presentation.YoutubeRoute
import com.example.musicapp.youtube.presentation.YoutubeViewModel

@Composable
fun ArtistScreen(
    modifier: Modifier = Modifier,
    artistId: String,
    youtubeViewModel: YoutubeViewModel,
    navController: NavHostController
) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    LaunchedEffect(artistId) {
        youtubeViewModel.loadArtist(artistId)
    }

    val isLoading by youtubeViewModel.isLoading.collectAsState()
    val artistPage by youtubeViewModel.artistPage.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            artistPage?.let {
                ArtistContent(
                    artistPage = it,
                    mediaControllerManager = mediaControllerManager,
                    navController = navController
                )
            }
        }
    }
}

@Preview
@Composable
private fun ArtistContentPreview() {
    MusicTheme {
        ArtistContent(
            artistPage = FakeModule.provideArtistPage(),
            mediaControllerManager = FakeModule.provideMediaControllerManager(),
            navController = rememberNavController()
        )
    }
}

@Composable
private fun ArtistContent(
    artistPage: ArtistPage,
    mediaControllerManager: MediaControllerManager,
    navController: NavHostController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                        thumbnailSource = ThumbnailSource.FromUrl(artistPage.artist.thumbnail),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = artistPage.artist.title,
                        modifier = Modifier.align(Alignment.BottomStart),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Black, fontSize = 56.sp
                        )
                    )
                }
            }

            itemsIndexed(items = artistPage.sections) { _, section ->
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium.copy(color = White),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 12.dp, end = 12.dp)
                )
                when (section.items.firstOrNull()) {
                    is SongItem -> {
                        section.items.forEach { item ->
                            if (item is SongItem) {
                                SongItemFromYoutube(modifier = Modifier.padding(
                                    vertical = 2.dp, horizontal = 12.dp
                                ),
                                    song = item,
                                    onClick = {
                                        mediaControllerManager.playYoutubeSong(item)
                                    })
                            }
                        }
                    }

                    is AlbumItem -> {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(items = section.items, key = { _, item ->
                                item.id
                            }) { _, item ->
                                if (item is AlbumItem) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(120.dp)
                                            .clickable {
                                                navController.navigate(YoutubeRoute.ALBUM + "/" + item.id)
                                            }
                                    ) {
                                        Thumbnail(
                                            thumbnailSource = ThumbnailSource.FromUrl(item.thumbnail),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1f)
                                        )
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = White, fontSize = 12.sp
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is ArtistItem -> {

                    }

                    is PlaylistItem -> {

                    }
                    null -> {

                    }
                }
            }
        }
    }

}
