package com.example.musicapp.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.viewmodels.CloudViewModel

@Composable
fun SettingScreen(viewModel: CloudViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFffe9f2)),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            viewModel.upload()
        }) {
            Text(text = "Upload")
        }
    }
}