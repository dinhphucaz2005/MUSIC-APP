package com.example.musicapp.youtube.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.innertube.models.AlbumItem
import com.example.innertube.models.ArtistItem
import com.example.innertube.models.PlaylistItem
import com.example.innertube.models.SongItem
import com.example.innertube.models.YTItem
import com.example.innertube.pages.HomePage
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.R
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.core.presentation.components.MyListItem
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.core.presentation.theme.Black
import com.example.musicapp.core.presentation.theme.LightGray
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.core.presentation.theme.White
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.youtube.presentation.YoutubeRoute
import com.example.musicapp.youtube.presentation.YoutubeViewModel

@Preview
@Composable
private fun YoutubeScreenPreview() {
    MusicTheme {
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

    val home by youtubeViewModel.home.collectAsState()

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
                .background(Black)
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = home.sections) { _, section ->
                HomePageSection(
                    section = section,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onClick = { item ->
                        when (item) {
                            is AlbumItem -> TODO()
                            is ArtistItem -> TODO()
                            is PlaylistItem -> navController.navigate(YoutubeRoute.PLAYLIST_DETAIL + "/" + item.id)
                            is SongItem -> mediaControllerManager.playYoutubeSong(item)
                        }
                    }
                )
            }
        }
    }


}

val ListItemHeight = 60.dp

@Composable
private fun LazyItemScope.HomePageSection(
    section: HomePage.Section,
    modifier: Modifier,
    onClick: (YTItem) -> Unit
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MyListItem(
            modifier = Modifier.height(ListItemHeight),
            leadingContent = {
                section.thumbnail?.let {
                    Thumbnail(
                        thumbnailSource = ThumbnailSource.FromUrl(it),
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(DefaultCornerSize))
                            .background(LightGray)
                    )
                }
            },
            headlineContent = {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
            },
            supportingContent = {
                section.label?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        color = White
                    )
                }
            }
        )
        if (section.thumbnail != null) {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(items = section.items, key = { _, item -> item.id }) { _, item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(DefaultCornerSize))
                            .clickable(onClick = { onClick(item) })
                    ) {
                        Thumbnail(
                            contentScale = ContentScale.Crop,
                            thumbnailSource = ThumbnailSource.FromUrl(item.thumbnail),
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(DefaultCornerSize))
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = White,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                        )
                    }
                }
            }

        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(items = section.items, key = { _, item -> item.id }) { _, item ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(16f / 9)
                            .clip(RoundedCornerShape(DefaultCornerSize))
                            .background(LightGray)
                            .clickable(onClick = { onClick(item) })
                    ) {
                        Thumbnail(
                            thumbnailSource = ThumbnailSource.FromUrl(item.thumbnail),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

        }
    }
}
