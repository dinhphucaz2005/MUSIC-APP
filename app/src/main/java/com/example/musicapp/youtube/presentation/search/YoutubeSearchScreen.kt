package com.example.musicapp.youtube.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.core.presentation.components.DefaultSearchBar
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.youtube.presentation.screen.SongItemFromYoutube
import kotlinx.coroutines.launch

@Composable
fun YoutubeSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: YoutubeSearchViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val songListState by viewModel.songListState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {

        val state = rememberLazyListState()

        DefaultSearchBar(
            searchQuery = songListState.searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onImeSearch = {
                keyboardController?.hide()
                viewModel.onSearch()
                focusManager.clearFocus()
                scope.launch {
                    state.animateScrollToItem(0)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        LazyColumnWithAnimation2(
            state = state,
            items = songListState.searchResults,
        ) { itemModifier, _, item ->
            SongItemFromYoutube(
                modifier = itemModifier,
                song = item,
                onClick = {
                    mediaControllerManager.playYoutubeSong(item)
                }
            )
        }
    }
}