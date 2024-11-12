package com.example.musicapp.ui.screen.cloud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.domain.model.Song
import com.example.musicapp.ui.components.LazyColumnWithAnimation
import com.example.musicapp.ui.screen.home.SongItem
import com.example.musicapp.ui.screen.playlist.LoadingScreen
import com.example.musicapp.viewmodels.CloudViewModel

@Composable
fun CloudScreen(viewModel: CloudViewModel = hiltViewModel()) {
    val songs by viewModel.songs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    if (isLoading) {
        LoadingScreen(Modifier.fillMaxSize())
    } else {
        LazyColumnWithAnimation(
            modifier = Modifier.fillMaxSize(),
            items = songs,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) { itemModifier, _, item ->
            SongItem(itemModifier.clickable {
                viewModel.play(songs.indexOf(item))
            }, item as Song)
        }
    }
}