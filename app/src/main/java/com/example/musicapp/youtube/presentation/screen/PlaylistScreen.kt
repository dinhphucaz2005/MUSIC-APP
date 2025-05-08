package com.example.musicapp.youtube.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
//import com.example.innertube.models.Artist
//import com.example.innertube.models.PlaylistItem
//import com.example.innertube.models.SongItem
//import com.example.innertube.models.WatchEndpoint
//import com.example.innertube.pages.PlaylistPage
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.ui.theme.LightGray
import com.example.musicapp.ui.theme.MyMusicAppTheme
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.other.domain.model.YoutubeSong
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.youtube.presentation.YoutubeViewModel

@Preview
@Composable
private fun PlaylistScreenPreview() {

//    val playlistItem = PlaylistItem(
//        title = "Liked Music",
//        thumbnail = "",
//        id = "",
//        author = Artist("", ""),
//        songCountText = "",
//        playEndpoint = null,
//        shuffleEndpoint = WatchEndpoint(),
//        radioEndpoint = null,
//    )
    MyMusicAppTheme {
//        PlaylistContent(
//            playlist = PlaylistPage(playlistItem, List(20) {
//                SongItem(
//                    id = "id",
//                    title = "Song Title",
//                    artists = listOf(Artist("Artist", "id")),
//                    album = null,
//                    duration = 1000,
//                    thumbnail = "",
//                    explicit = false,
//                    endpoint = WatchEndpoint()
//                )
//            }, null, null),
//            modifier = Modifier.fillMaxSize(),
//            mediaControllerManager = FakeModule.mediaControllerManager
//        )
    }
}

//@Composable
//fun PlaylistScreen(
//    playlistId: String, navController: NavHostController, youtubeViewModel: YoutubeViewModel
//) {
//    val mediaControllerManager = LocalMediaControllerManager.current ?: return
//
//    LaunchedEffect(playlistId) {
//        youtubeViewModel.loadPlaylist(playlistId)
//    }
//
//    val playlist by youtubeViewModel.playlist.collectAsState()
//    val isLoading by youtubeViewModel.isLoading.collectAsState()
//
//    val resetPlaylist = {
//        youtubeViewModel.resetPlaylist()
//        navController.popBackStack()
//    }
//
//    BackHandler(onBack = { resetPlaylist() })
//
//
//    Scaffold(
//        topBar = {
//            Row(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                CommonIcon(
//                    icon = R.drawable.ic_back,
//                    onClick = { resetPlaylist() }
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                CommonIcon(icon = R.drawable.ic_search,
//                    onClick = { /*TODO("Implement search in playlist")*/ })
//            }
//        },
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxSize(),
//        containerColor = Color.Transparent
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(top = 8.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator()
//            }
//            playlist?.let {
//                PlaylistContent(
//                    playlist = it,
//                    modifier = Modifier.fillMaxSize(),
//                    mediaControllerManager = mediaControllerManager,
//                )
//            }
//        }
//    }
//}

//@Composable
//fun PlaylistContent(
//    playlist: PlaylistPage,
//    modifier: Modifier = Modifier,
//    mediaControllerManager: MediaControllerManager,
//) {
//    LazyColumn(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        item {
//            Thumbnail(
//                thumbnailSource = ThumbnailSource.FromUrl(playlist.playlist.thumbnail),
//                modifier = Modifier
//                    .size(220.dp)
//                    .clip(RoundedCornerShape(DefaultCornerSize))
//                    .background(Color(0xFF5b9599))
//            )
//        }
//        item {
//            Text(
//                text = playlist.playlist.title,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                style = MaterialTheme.typography.titleLarge,
//                color = White,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//        }
//        item {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CommonIcon(
//                    icon = R.drawable.ic_shuffle,
//                    size = 24.dp
//                )
//                Text(
//                    text = "Blissful Soul",
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = White,
//                )
//            }
//        }
//        item {
//            Text(
//                text = "2 wk ago",
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                style = MaterialTheme.typography.labelMedium,
//                color = LightGray,
//            )
//        }
//        item {
//            Text(
//                text = "Music that you like any Youtube app will be show here. You can change this in Settings.",
//                overflow = TextOverflow.Ellipsis,
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.labelMedium,
//                color = LightGray,
//                modifier = Modifier.padding(horizontal = 50.dp)
//            )
//        }
//        item {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CommonIcon(
//                    icon = R.drawable.ic_play,
//                    size = 32.dp,
//                    onClick = { })
//                CommonIcon(
//                    icon = R.drawable.ic_play,
//                    size = 48.dp,
//                    onClick = { })
//                CommonIcon(
//                    icon = R.drawable.ic_play,
//                    size = 32.dp,
//                    onClick = { })
//            }
//        }
//        itemsIndexed(playlist.songs) { index, song ->
//            SongItemFromYoutube(
//                modifier = Modifier.padding(
//                    horizontal = 8.dp
//                ), song = song, onClick = {
//                    mediaControllerManager.playQueue(
//                        songs = playlist.songs.map { YoutubeSong(it) },
//                        index = index,
//                        id = Queue.YOUTUBE_PLAYLIST_ID + "/" + playlist.playlist.id
//                    )
//                }
//            )
//        }
//    }
//}