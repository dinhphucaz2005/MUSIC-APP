package nd.phuc.musicapp.youtube.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import nd.phuc.core.presentation.theme.MyMusicAppTheme

//@Composable
//fun AlbumScreen(
//    modifier: Modifier = Modifier, albumId: String, youtubeViewModel: YoutubeViewModel
//) {
//
//    LaunchedEffect(albumId) { youtubeViewModel.loadAlbum(albumId) }
//
//    val isLoading by youtubeViewModel.isLoading.collectAsState()
//    val albumPage by youtubeViewModel.albumPage.collectAsState()
//
//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        if (isLoading) {
//            CircularProgressIndicator()
//        } else {
//            albumPage?.let {
//                AlbumContent(
//                    albumPage = it
//                )
//            }
//        }
//    }
//}

@Preview
@Composable
private fun ArtistContentPreview() {
    MyMusicAppTheme {
//        AlbumContent(FakeModule.provideAlbumPage())
    }
}

//@Composable
//private fun AlbumContent(albumPage: AlbumPage) {
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        item {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f)
//            ) {
//                Thumbnail(
//                    thumbnailSource = ThumbnailSource.FromUrl(albumPage.album.thumbnail),
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )
//                Text(
//                    text = albumPage.album.title,
//                    modifier = Modifier.align(Alignment.BottomStart),
//                    style = MaterialTheme.typography.titleLarge.copy(
//                        color = Black, fontSize = 56.sp
//                    )
//                )
//            }
//        }
//
//        itemsIndexed(items = albumPage.songs, key = { _, item -> item.id }) { index, item ->
//            val offsetX = remember { Animatable(100f) }
//
//            LaunchedEffect(item) {
//                if (offsetX.value != 0f) {
//                    offsetX.animateTo(
//                        targetValue = 0f,
//                        animationSpec = tween(durationMillis = 200)
//                    )
//                }
//            }
//
//            SongItemFromYoutube(
//                modifier = Modifier.offset {
//                    IntOffset(offsetX.value.toInt(), 0)
//                },
//                song = item,
//                onClick = {
//                    TODO()
////                    mediaControllerManager.playYoutubePlaylist(
////                        playlistId = albumPage.album.playlistId,
////                        songs = albumPage.songs,
////                        index = index
////                    )
//                }
//            )
//        }
//
//    }
//}
