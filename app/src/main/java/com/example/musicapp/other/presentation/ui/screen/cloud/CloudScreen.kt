package com.example.musicapp.other.presentation.ui.screen.cloud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.presentation.ui.screen.home.SongItemContent
import com.example.musicapp.other.presentation.ui.screen.playlist.LoadingScreen
import com.example.musicapp.other.viewmodels.CloudViewModel

@Composable
fun CloudScreen(viewModel: CloudViewModel = hiltViewModel()) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val songs by viewModel.songs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    if (isLoading) {
        LoadingScreen(Modifier.fillMaxSize())
    } else {
        LazyColumnWithAnimation(
            modifier = Modifier.fillMaxSize(),
            items = songs,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) { itemModifier, index, item ->
            SongItemContent(itemModifier.clickable {
                val queue = Queue.Builder()
                    .setId(Queue.FIREBASE_ID)
                    .setOtherSongs(songs)
                    .setIndex(index)
                    .build()
                mediaControllerManager.playQueue(queue)
            }, item as Song)
        }
    }
}