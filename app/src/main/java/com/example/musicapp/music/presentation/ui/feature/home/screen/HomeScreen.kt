package com.example.musicapp.music.presentation.ui.feature.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.core.presentation.previews.ExtendDevicePreviews
import com.example.musicapp.di.fakeViewModel
import com.example.musicapp.music.presentation.ui.feature.home.HomeViewModel
import com.example.musicapp.music.presentation.ui.feature.home.components.YourSongsSection
import com.example.musicapp.ui.theme.MyMusicAppTheme


@ExperimentalMaterial3Api
@UnstableApi
@ExtendDevicePreviews
@Composable
private fun Preview() {
    MyMusicAppTheme {
        HomeScreen(
            homeViewModel = fakeViewModel<HomeViewModel>(),
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
) {
    val songs by homeViewModel.songs.collectAsState()
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = scrollState,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            YourSongsSection(
                songs = songs
            )
        }

    }
}
