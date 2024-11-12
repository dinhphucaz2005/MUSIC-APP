package com.example.musicapp.ui.screen.youtube

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.viewmodels.YoutubeViewModel

@Composable
fun YoutubeScreen(modifier: Modifier = Modifier, viewModel: YoutubeViewModel = hiltViewModel()) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFf5bedb))
    ) {

    }
}