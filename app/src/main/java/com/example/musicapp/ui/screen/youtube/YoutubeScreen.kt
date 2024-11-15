package com.example.musicapp.ui.screen.youtube

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.innertube.models.SongItem
import com.example.musicapp.constants.DefaultShape
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.viewmodels.YoutubeViewModel

@Preview
@Composable
private fun YoutubeScreenPreview() {
    MusicTheme {
        YoutubeScreen(viewModel = FakeModule.provideYoutubeViewModel())
    }
}

@Composable
fun YoutubeScreen(modifier: Modifier = Modifier, viewModel: YoutubeViewModel) {

    val home by viewModel.home.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = home.sections) { _, section ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                ListItem(headlineContent = {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }, supportingContent = {
                    section.label?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }, colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                    supportingColor = MaterialTheme.colorScheme.primary,
                    headlineColor = MaterialTheme.colorScheme.primary
                )
                )

                val state = rememberPagerState(
                    pageCount = {
                        section.items.size
                    },
                    initialPage = 0
                )
                HorizontalPager(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) { page ->
                    val item: SongItem? = when (section.items[page]) {
                        is SongItem -> section.items[page] as SongItem
                        else -> null
                    }
                    if (item != null) {
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
                                    .height(200.dp)
                                    .clip(DefaultShape)
                                    .background(Color(0xFFeba6c7))
                            )
                            ListItem(
                                headlineContent = {
                                    Text(
                                        item.title, modifier = Modifier.fillMaxWidth(),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                supportingContent = {
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
    }


}