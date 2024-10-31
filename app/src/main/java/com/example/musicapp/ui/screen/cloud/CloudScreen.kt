package com.example.musicapp.ui.screen.cloud

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.CloudRepository
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.viewmodels.CloudViewModel

@Preview
@Composable
private fun Preview() {
    MusicTheme {
        CloudScreen(viewModel = CloudViewModel(object : CloudRepository {
            override suspend fun loadSongs(
                title: String,
                page: Int,
                size: Int
            ): AppResource<List<Song>> {
                TODO("Not yet implemented")
            }
        }))
    }
}

@OptIn(UnstableApi::class)
@Composable
fun CloudScreen(modifier: Modifier = Modifier, viewModel: CloudViewModel) {

    val songs = viewModel.songs
    val colors = listOf(
        listOf(Color(0xFF2b4ea2), Color(0xFF1ee3f0)),
        listOf(Color(0xFFda2b56), Color(0xFFf69d3f)),
        listOf(Color(0xFF2bb7b9), Color(0xFFe4f686)),
        listOf(Color(0xFFa47bff), Color(0xFF6fcfff)),
        listOf(Color(0xFF8e8e8e), Color(0xFFd1d1d1)),
        listOf(Color(0xFFeabcf5), Color(0xFFb9fafe))
    )


    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (songList) = createRefs()
        LazyColumn(
            modifier = Modifier.constrainAs(songList) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(items = songs) { index, song ->
                SongItem(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    song,
                    colors[index % colors.size]
                )
            }
        }
    }


}