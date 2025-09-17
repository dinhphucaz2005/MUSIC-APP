/*
package nd.phuc.musicapp.other.presentation.ui.screen.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nd.phuc.musicapp.R
import nd.phuc.core.model.DefaultCornerSize
import nd.phuc.core.model.Playlist
import nd.phuc.core.presentation.theme.Black


@Composable
fun PlayListHome(
    playlistViewModel: PlaylistViewModel,
    navController: NavController,
//    songViewModel: SongViewModel = hiltViewModel()
) {

//    val playlists by playlistViewModel.playlists.collectAsState()
//    val likedSongs by songViewModel.likedSongs.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(PlaylistRoute.CREATE_NEW_PLAYLIST) },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = null
                )
            }
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = stringResource(R.string.saved_playlist) + " \u2022 ${0*/
/*playlists.size*//*
} playlists",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(contentPadding)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
//                items(items = playlists, key = { item ->
//                    item.id
//                }) { item ->
//                    PlaylistItem(item, navController)
//                }
            }

//            LikedPlayListContent(
//                songs = likedSongs, modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            )
        }
    }
}

@Composable
fun PlaylistItem(item: Playlist, navController: NavController) {
    ListItem(
        leadingContent = {
            Image(
                painter = painterResource(R.drawable.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(DefaultCornerSize))
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        },
        headlineContent = {
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    navController.navigate(PlaylistRoute.DETAIL + "/" + item.id)
                })
            },
        colors = ListItemDefaults.colors(
            headlineColor = MaterialTheme.colorScheme.primary,
            overlineColor = MaterialTheme.colorScheme.primary,
            containerColor = Black,
            supportingColor = MaterialTheme.colorScheme.primary,
            leadingIconColor = MaterialTheme.colorScheme.primary,
            trailingIconColor = MaterialTheme.colorScheme.primary,
        )
    )
}




//@Composable
//fun LikedPlayListContent(songs: List<Song>, modifier: Modifier = Modifier) {
//
//    val mediaControllerManager = LocalMediaControllerManager.current ?: return
//
//    val menuState = LocalMenuState.current
//
//    Column(
//        modifier = modifier
//    ) {
//
//        Text(
//            text = stringResource(R.string.favourite_songs) + " \u2022 ${songs.size} songs",
//            color = MaterialTheme.colorScheme.primary,
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//        )
//
//        LazyColumnWithAnimation2(
//            items = songs,
//            key = { _, item -> item.id },
//            modifier = Modifier
//                .fillMaxSize()
//        ) { itemModifier, index, song ->
//            SongItemContent(
//                song = song,
//                modifier = itemModifier.clickable(onClick = {
//                    mediaControllerManager.playQueue(
//                        songs = songs,
//                        index = index,
//                        "${Queue.LIKED_PLAYLIST_ID}/${songs.size}"
//                    )
//                }),
//                onSongClick = {
//                    menuState.show {
//                        LikedPlaylistMoreChoiceContent(song) {
//                            menuState.dismiss()
//                        }
//                    }
//                },
//                onMoreChoice = {}
//            )
//        }
//
//    }
//}
//
//@Preview
//@Composable
//private fun MoreChoiceContentPreview() {
//    MyMusicAppTheme {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(black)
//        ) {
//            LikedPlaylistMoreChoiceContent(
//                playlistViewModel = FakeModule.providePlaylistViewModel(),
//                song = FakeModule.localSong
//            ) {}
//        }
//    }
//}
//
//@Composable
//private fun LikedPlaylistMoreChoiceContent(
//    song: Song,
//    playlistViewModel: PlaylistViewModel = hiltViewModel(),
//    dismiss: () -> Unit,
//) {
//    val mediaControllerManager = LocalMediaControllerManager.current ?: return
//
//    val lists = listOf(
//        Triple(R.drawable.ic_disc, R.string.add_to_playlist) { TODO() },
//        Triple(R.drawable.ic_disc, R.string.remove_from_playlist) {
//            playlistViewModel.deleteLikedSong(song)
//        },
//        Triple(R.drawable.ic_disc, R.string.add_to_next) { mediaControllerManager.addToNext(song) },
//        Triple(
//            R.drawable.ic_disc,
//            R.string.add_to_queue
//        ) { mediaControllerManager.addToQueue(song) }
//    )
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .clip(RoundedCornerShape(topStart = DefaultCornerSize, topEnd = DefaultCornerSize))
//            .background(darkGray)
//    ) {
//        MiniSongItemContent(song = song)
//        HorizontalDivider(thickness = 2.dp, modifier = Modifier.fillMaxWidth(), color = lightGray)
//        lists.fastForEach { (icon, text, action) ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .height(52.dp)
//                    .clickable(onClick = {
//                        action()
//                        dismiss()
//                    }),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    painter = painterResource(id = icon),
//                    contentDescription = null,
//                    modifier = Modifier.size(24.dp),
//                    tint = white
//                )
//                Text(
//                    text = stringResource(id = text),
//                    style = MaterialTheme.typography.titleMedium,
//                    color = white,
//                    modifier = Modifier.padding(start = 16.dp)
//                )
//            }
//        }
//    }
//
//}
*/
