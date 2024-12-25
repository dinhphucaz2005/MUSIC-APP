package com.example.musicapp.other.presentation.ui.screen.playlist

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.viewmodels.PlaylistViewModel

@SuppressLint("UnsafeOptInUsageError")
@Preview
@Composable
fun PlaylistDetailPreview() {
    MusicTheme {
        PlayListDetail(
            playlistId = "5092",
            navController = NavHostController(LocalContext.current),
            viewModel = FakeModule.providePlaylistViewModel(),
        )
    }
}

@Composable
fun PlayListDetail(
    playlistId: String?,
    navController: NavHostController,
    viewModel: PlaylistViewModel,
) {
}