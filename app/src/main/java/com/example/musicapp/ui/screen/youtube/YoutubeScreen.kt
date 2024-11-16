package com.example.musicapp.ui.screen.youtube

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.innertube.pages.HomePage
import com.example.musicapp.R
import com.example.musicapp.constants.DefaultShape
import com.example.musicapp.constants.SectionHeight
import com.example.musicapp.constants.TopBarHeight
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.components.CommonIcon
import com.example.musicapp.ui.components.MyListItem
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.viewmodels.YoutubeViewModel

@Preview
@Composable
private fun YoutubeScreenPreview() {
    val navController = rememberNavController()
    MusicTheme {
        YoutubeScreen(
            viewModel = FakeModule.provideYoutubeViewModel(),
            navController = navController
        )
    }
}

@Composable
fun YoutubeScreen(
    modifier: Modifier = Modifier,
    viewModel: YoutubeViewModel,
    navController: NavHostController
) {

    val home by viewModel.home.collectAsState()
    val lazyColumnState = rememberLazyListState()

    var previousOffset by remember { mutableIntStateOf(0) }
    val isScrollingUp = remember {
        derivedStateOf {
            val currentOffset = lazyColumnState.firstVisibleItemScrollOffset
            val isScrollingUp = currentOffset < previousOffset
            previousOffset = currentOffset
            isScrollingUp
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        AnimatedVisibility(
            visible = isScrollingUp.value,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TopBarHeight),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                )
                Text(
                    text = "Music",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                CommonIcon(icon = R.drawable.ic_search)
                CommonIcon(icon = R.drawable.ic_setting)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 12.dp),
            state = lazyColumnState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(items = home.sections) { _, section ->
                SectionItem(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), section = section,
                    viewModel = viewModel
                )
            }
        }

    }
}

@Composable
fun SectionItem(
    modifier: Modifier, section: HomePage.Section,
    viewModel: YoutubeViewModel
) {
    when (section) {
        is HomePage.Section.Song -> SongSectionItem(modifier, section, viewModel = viewModel)
        is HomePage.Section.Album -> TODO()
        is HomePage.Section.Artist -> TODO()
        is HomePage.Section.Playlist -> PlaylistSectionItem(modifier, section)
    }
}

@Composable
fun SongSectionItem(
    modifier: Modifier = Modifier,
    section: HomePage.Section.Song,
    viewModel: YoutubeViewModel
) {
    if (section.label != null) {
        Column(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            MyListItem(headlineContent = {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }, supportingContent = {
                Text(
                    text = section.label!!,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }, leadingContent = {
                AsyncImage(
                    model = section.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }, modifier = Modifier.fillMaxWidth())

            val row = section.items.size.div(3)
            val col = 3
            for (i in 0..<row) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    for (j in 0..<col) {
                        val index = i * col + j
                        Box(
                            modifier
                                .padding(end = if (j == col - 1) 0.dp else 8.dp)
                                .weight(1f)
                                .clip(DefaultShape)
                                .aspectRatio(1f)
                                .clickable {
                                }
                        ) {
                            AsyncImage(
                                model = section.items[index].thumbnail,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = section.items[index].title,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(start = 20.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }, colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                    supportingColor = MaterialTheme.colorScheme.primary,
                    headlineColor = MaterialTheme.colorScheme.primary
                )
            )

            val state = rememberPagerState(
                pageCount = {
                    section.items.size
                }, initialPage = 0
            )
            HorizontalPager(
                state = state,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) { page ->
                val item = section.items[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    AsyncImage(
                        model = item.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(SectionHeight)
                            .clip(DefaultShape)
                            .background(Color(0xFFeba6c7))
                    )
                    ListItem(headlineContent = {
                        Text(
                            item.title,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }, modifier = Modifier.fillMaxWidth(), supportingContent = {
                        Text(
                            text = item.artists.joinToString { it.name },
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }, colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                        supportingColor = MaterialTheme.colorScheme.primary,
                        headlineColor = MaterialTheme.colorScheme.primary
                    )
                    )
                }
            }
        }

    }
}

@Composable
fun PlaylistSectionItem(modifier: Modifier = Modifier, section: HomePage.Section.Playlist) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(SectionHeight)
    ) {
        ListItem(headlineContent = {
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }, supportingContent = {
            Text(
                text = section.label ?: "",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        })
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(section.items) { _, item ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null,
                        model = item.thumbnail,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
