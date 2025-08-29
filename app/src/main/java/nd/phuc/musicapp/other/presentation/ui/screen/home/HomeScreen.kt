package nd.phuc.musicapp.other.presentation.ui.screen.home


//import android.annotation.SuppressLint
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.util.fastForEach
//import androidx.media3.common.util.UnstableApi
//import nd.phuc.musicapp.LocalMediaControllerManager
//import nd.phuc.musicapp.LocalMenuState
//import nd.phuc.musicapp.R
//import nd.phuc.musicapp.constants.DefaultCornerSize
//import nd.phuc.musicapp.core.presentation.components.MyListItem
//import nd.phuc.musicapp.core.presentation.components.Thumbnail
//import nd.phuc.musicapp.di.FakeModule
//import nd.phuc.musicapp.other.domain.model.LocalSong
//import nd.phuc.musicapp.other.domain.model.Queue
//import nd.phuc.musicapp.other.viewmodels.HomeViewModel
//import nd.phuc.musicapp.song.MiniSongItemContent
//import nd.phuc.musicapp.song.SongItemContent
//import nd.phuc.musicapp.ui.theme.black
//import nd.phuc.musicapp.ui.theme.darkGray
//import nd.phuc.musicapp.ui.theme.lightGray
//import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
//import nd.phuc.musicapp.ui.theme.white
//import nd.phuc.musicapp.util.MediaControllerManager
//
//
//@ExperimentalMaterial3Api
//@UnstableApi
//@Preview
//@Composable
//fun Preview() {
//    MyMusicAppTheme {
//        Column {
//            HomeContent(
//                modifier = Modifier.fillMaxSize(),
//                mediaControllerManager = FakeModule.mediaControllerManager,
//                songs = List(20) { index -> FakeModule.localSong.copy(id = "$index") }
//            )
//        }
//    }
//}
//
//
//@SuppressLint("InlinedApi")
//@UnstableApi
//@Composable
//fun HomeScreen(
//    homeViewModel: HomeViewModel
//) {
//
//    val mediaControllerManager = LocalMediaControllerManager.current ?: return
//
//    val playBackState by mediaControllerManager.playBackState.collectAsState()
//    val currentSong by mediaControllerManager.currentSong.collectAsState()
//    val songs by homeViewModel.songs.collectAsState()
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        MyListItem(
//            modifier = Modifier
//                .padding(vertical = 12.dp)
//                .fillMaxWidth()
//                .height(120.dp),
//            headlineContent = {
//                Text(
//                    text = currentSong.data.getSongTitle(),
//                    style = MaterialTheme.typography.titleLarge,
//                    color = white,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 2,
//                )
//            },
//            supportingContent = {
//                Text(
//                    text = currentSong.data.getSongArtist(),
//                    style = MaterialTheme.typography.titleMedium,
//                    color = white
//                )
//            },
//            leadingContent = {
//                Thumbnail(
//                    Modifier
//                        .fillMaxHeight()
//                        .aspectRatio(1f)
//                        .clip(RoundedCornerShape(DefaultCornerSize)),
//                    contentScale = ContentScale.Crop,
//                    thumbnailSource = currentSong.data.getThumbnail()
//                )
//            }
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            val iconModifier = Modifier
//                .size(40.dp)
//                .padding(8.dp)
//
//            listOf(
//                playBackState.playerState.resource,
//                R.drawable.ic_skip_back,
//                playBackState.playerState.resource,
//                R.drawable.ic_skip_forward,
//                playBackState.loopMode.resource
//            ).forEachIndexed { index, resource ->
//                IconButton(onClick = {
//                    when (index) {
//                        0 -> mediaControllerManager.updateLoopMode()
//                        1 -> mediaControllerManager.playPreviousSong()
//                        2 -> mediaControllerManager.togglePlayPause()
//                        3 -> mediaControllerManager.playNextSong()
//                        4 -> mediaControllerManager.updateShuffleMode()
//                    }
//                }) {
//                    Icon(
//                        painter = painterResource(id = resource),
//                        contentDescription = null,
//                        modifier = iconModifier,
//                    )
//                }
//            }
//        }
//
//        HorizontalDivider(
//            thickness = 2.dp,
//            color = white
//        )
//
//        HomeContent(
//            modifier = Modifier.fillMaxSize(),
//            mediaControllerManager = mediaControllerManager,
//            songs = songs
//        )
//    }
//
//
//}
//
//@UnstableApi
//@Composable
//private fun HomeContent(
//    modifier: Modifier = Modifier,
//    mediaControllerManager: MediaControllerManager,
//    songs: List<LocalSong>,
//) {
//    val menuState = LocalMenuState.current
//
//    Column(
//        modifier = modifier.fillMaxSize()
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            itemsIndexed(items = songs) { index, song ->
//                SongItemContent(
//                    modifier = Modifier, song = song,
//                    onSongClick = {
//                        mediaControllerManager.playQueue(
//                            songs = songs,
//                            index = index,
//                            id = Queue.LOCAL_ID
//                        )
//                    }
//                ) {
//                    menuState.show {
//                        HomeScreenMoreChoiceContent(
//                            song = song,
//                            dismiss = menuState::dismiss,
//                            mediaControllerManager = mediaControllerManager
//                        )
//                    }
//                }
//            }
//        }
//
////        LazyColumnWithAnimation2(
////            modifier = Modifier
////                .fillMaxWidth()
////                .weight(1f),
////            items = songs,
////            verticalArrangement = Arrangement.spacedBy(8.dp),
////            key = { _, item -> item.id }
////        ) { itemModifier, index, song ->
////            SongItemContent2(
////                modifier = itemModifier, song = song,
////                onSongClick = {
////                    mediaControllerManager.playQueue(
////                        songs = songs,
////                        index = index,
////                        id = Queue.LOCAL_ID
////                    )
////                }
////            ) {
////                menuState.show {
////                    if (song is LocalSong) {
////                        HomeScreenMoreChoiceContent(
////                            song = song,
////                            dismiss = menuState::dismiss,
////                            mediaControllerManager = mediaControllerManager
////                        )
////                    }
////                }
////            }
////        }
//    }
//}
//
//@Preview
//@Composable
//private fun HomeScreenMoreChoiceContentPreview() {
//    MyMusicAppTheme {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(black)
//        ) {
//            HomeScreenMoreChoiceContent(
//                song = FakeModule.localSong,
//                dismiss = { },
//                mediaControllerManager = FakeModule.mediaControllerManager,
//            )
//        }
//    }
//}
//
//@Composable
//private fun HomeScreenMoreChoiceContent(
//    song: LocalSong,
//    dismiss: () -> Unit,
//    mediaControllerManager: MediaControllerManager
//) {
//
//
//    val lists = listOf(
//        Triple(R.drawable.ic_disc, R.string.add_to_playlist) { TODO() },
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