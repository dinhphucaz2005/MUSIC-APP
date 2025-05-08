package com.example.musicapp.youtube.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.theme.black
import com.example.musicapp.ui.theme.MyMusicAppTheme
import com.example.musicapp.youtube.presentation.YoutubeRoute
import com.example.musicapp.youtube.presentation.YoutubeViewModel

@Preview
@Composable
private fun YoutubeScreenPreview() {
    MyMusicAppTheme {
        YoutubeScreen(
            navController = rememberNavController(),
            youtubeViewModel = FakeModule.provideYoutubeViewModel()
        )
    }
}

@Composable
fun YoutubeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    youtubeViewModel: YoutubeViewModel
) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

//    val home by youtubeViewModel.home.collectAsState()

    Scaffold(
        topBar = {
            Row {
                Text(
                    text = "Cuhp Tube",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                CommonIcon(
                    icon = R.drawable.ic_search,
                    onClick = {
                        navController.navigate(YoutubeRoute.SEARCH)
                    }
                )

            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .background(black)
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TODO("Implement the rest of the screen")
//            itemsIndexed(items = home.sections) { _, section ->
//                HomePageSection(
//                    section = section,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight(),
//                    onClick = { item ->
//                        when (item) {
//                            is AlbumItem -> navController.navigate(YoutubeRoute.ALBUM + "/" + item.id)
//                            is ArtistItem -> navController.navigate(YoutubeRoute.ARTIST + "/" + item.id)
//                            is PlaylistItem -> navController.navigate(YoutubeRoute.PLAYLIST_DETAIL + "/" + item.id)
//
//                            is SongItem -> mediaControllerManager.playYoutubeSong(item)
//                        }
//                    }
//                )
//            }
        }
    }


}

val ListItemHeight = 60.dp

//@Composable
//private fun HomePageSection(
//    section: HomePage.Section,
//    modifier: Modifier,
//    onClick: (YTItem) -> Unit
//) {
//    Column(
//        modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        MyListItem(
//            modifier = Modifier.height(ListItemHeight),
//            leadingContent = {
//                section.thumbnail?.let {
//                    Thumbnail(
//                        thumbnailSource = ThumbnailSource.FromUrl(it),
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .aspectRatio(1f)
//                            .clip(RoundedCornerShape(DefaultCornerSize))
//                            .background(LightGray)
//                    )
//                }
//            },
//            headlineContent = {
//                Text(
//                    text = section.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    color = White
//                )
//            },
//            supportingContent = {
//                section.label?.let {
//                    Text(
//                        text = it,
//                        style = MaterialTheme.typography.labelMedium,
//                        color = White
//                    )
//                }
//            }
//        )
//        if (section.thumbnail != null) {
//            LazyHorizontalGrid(
//                rows = GridCells.Fixed(3),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f),
//                verticalArrangement = Arrangement.spacedBy(6.dp),
//                horizontalArrangement = Arrangement.spacedBy(6.dp)
//            ) {
//                itemsIndexed(items = section.items, key = { _, item -> item.id }) { _, item ->
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .aspectRatio(1f)
//                            .clip(RoundedCornerShape(DefaultCornerSize))
//                            .clickable(onClick = { onClick(item) })
//                    ) {
//                        Thumbnail(
//                            contentScale = ContentScale.Crop,
//                            thumbnailSource = ThumbnailSource.FromUrl(item.thumbnail),
//                            modifier = Modifier
//                                .aspectRatio(1f)
//                                .clip(RoundedCornerShape(DefaultCornerSize))
//                        )
//                        Text(
//                            text = item.title,
//                            style = MaterialTheme.typography.labelSmall,
//                            color = White,
//                            overflow = TextOverflow.Ellipsis,
//                            maxLines = 1,
//                            modifier = Modifier
//                                .align(Alignment.BottomEnd)
//                                .padding(4.dp)
//                        )
//                    }
//                }
//            }
//
//        } else {
//            BoxWithConstraints(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            ) {
//                LazyRow(
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .wrapContentHeight(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    val maxWidth = maxWidth
//                    itemsIndexed(
//                        items = section.items,
//                        key = { _, item -> item.id }
//                    ) { _, item ->
//                        val (width: Dp, aspectRatio: Float) = when (item) {
//                            is SongItem -> Pair(maxWidth - 80.dp, 16f / 9)
//                            is AlbumItem -> Pair(maxWidth / 2, 1f)
//                            is PlaylistItem -> Pair(maxWidth / 2, 1f)
//                            else -> Pair(maxWidth, 1f)
//                        }
//                        Column(
//                            modifier = Modifier
//                                .width(width)
//                                .clickable(onClick = { onClick(item) })
//                        ) {
//
//                            Thumbnail(
//                                thumbnailSource = ThumbnailSource.FromUrl(item.thumbnail),
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .aspectRatio(aspectRatio)
//                                    .clip(RoundedCornerShape(DefaultCornerSize)),
//                                contentScale = ContentScale.Crop
//                            )
//
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(top = 6.dp)
//                                    .height(40.dp)
//                            ) {
//                                Text(
//                                    text = item.title,
//                                    style = MaterialTheme.typography.titleMedium,
//                                    color = White,
//                                    modifier = Modifier.fillMaxWidth(),
//                                    overflow = TextOverflow.Ellipsis,
//                                    maxLines = 2
//                                )
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//    }
//}
