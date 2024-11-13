package com.example.musicapp.ui.screen.youtube

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.domain.model.Song
import com.example.musicapp.ui.components.LazyColumnWithAnimation
import com.example.musicapp.ui.screen.home.SongItem
import com.example.musicapp.viewmodels.YoutubeViewModel

@Composable
fun YoutubeScreen(modifier: Modifier = Modifier, viewModel: YoutubeViewModel = hiltViewModel()) {

    val songs by viewModel.songs.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumnWithAnimation(
            modifier = Modifier.fillMaxSize(), songs,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) { modifier, index, item ->
            SongItem(
                modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { viewModel.play(index) })
                    },
                item as Song,
            )
        }
    }
}