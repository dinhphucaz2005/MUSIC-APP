package com.example.musicapp.youtube.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.core.presentation.components.DefaultSearchBar
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.youtube.presentation.componenets.SongItem
import com.example.musicapp.youtube.presentation.home.YoutubeViewModel

@Composable
fun YoutubeSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: YoutubeSearchViewModel = hiltViewModel(),
    youtubeViewModel: YoutubeViewModel = hiltViewModel()
) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val songListState by viewModel.songListState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxSize()) {
        DefaultSearchBar(
            searchQuery = songListState.searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onImeSearch = {
                keyboardController?.hide()
                viewModel.onSearch()
            },
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumnWithAnimation2(
            items = songListState.searchResults,
        ) { itemModifier, _, item ->
            SongItem(
                modifier = itemModifier,
                song = item,
                onClick = {
                    mediaControllerManager.playYoutubeSong(item)
                }
            )
        }
    }
}