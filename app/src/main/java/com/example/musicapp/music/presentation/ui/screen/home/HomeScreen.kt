package com.example.musicapp.music.presentation.ui.screen.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.di.fakeViewModel
import com.example.musicapp.music.presentation.ui.screen.home.components.FeaturedSection
import com.example.musicapp.music.presentation.ui.screen.home.components.RecentlyPlayedSection
import com.example.musicapp.music.presentation.ui.screen.home.components.SearchAnythingHeader
import com.example.musicapp.music.presentation.ui.screen.home.components.YourPlaylistsSection
import com.example.musicapp.music.presentation.ui.screen.home.components.YourSongsSection
import com.example.musicapp.ui.theme.MyMusicAppTheme


@ExperimentalMaterial3Api
@UnstableApi
@Preview
@Composable
fun Preview() {
    MyMusicAppTheme {
        HomeScreen(
            homeViewModel = fakeViewModel<HomeViewModel>(),
            onNavigateToAllLocalSongScreen = {}
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onNavigateToAllLocalSongScreen: () -> Unit,
) {
    val localSongState by homeViewModel.localSongState.collectAsState()
    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) { homeViewModel.loadSongs() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = scrollState,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { SearchAnythingHeader(onSearchClick = { }) }

        item { FeaturedSection() }

        item { RecentlyPlayedSection() }

        item { YourPlaylistsSection() }

        item {
            YourSongsSection(
                localSongState = localSongState,
                onSeeAllClick = onNavigateToAllLocalSongScreen
            )
        }

    }
}
