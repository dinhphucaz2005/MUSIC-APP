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
import com.example.musicapp.LocalMenuState
import com.example.musicapp.core.presentation.components.LazyColumnWithAnimation2
import com.example.musicapp.core.presentation.components.LoadingScreen
import com.example.musicapp.other.domain.model.Queue
import com.example.musicapp.other.presentation.ui.screen.home.SongItemContent
import com.example.musicapp.other.viewmodels.CloudViewModel

@Composable
fun CloudScreen(viewModel: CloudViewModel = hiltViewModel()) {

    val mediaControllerManager = LocalMediaControllerManager.current ?: return

    val songs by viewModel.songs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    if (isLoading) {
        LoadingScreen()
    } else {

        val menuState = LocalMenuState.current

        LazyColumnWithAnimation2(
            modifier = Modifier.fillMaxSize(),
            items = songs,
            key = { _, item -> item.id },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) { itemModifier, index, item ->
            SongItemContent(
                modifier = itemModifier.clickable(onClick = {
                    mediaControllerManager.playQueue(songs = songs, index = index, id = Queue.FIREBASE_ID)
                }),
                song = item,
                menuState = menuState
            )
        }
    }
}