package com.example.musicapp.song.presetation.song

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.musicapp.LocalMediaControllerManager
import com.example.musicapp.constants.QueuePeekHeight
import com.example.musicapp.core.presentation.components.MiniPlayer
import com.example.musicapp.other.viewmodels.SongViewModel
import com.example.musicapp.song.presetation.components.BottomSheet
import com.example.musicapp.song.presetation.components.BottomSheetState
import com.example.musicapp.song.presetation.components.rememberBottomSheetState

@Composable
fun BottomSheetPlayer(
    state: BottomSheetState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val playerConnection = LocalMediaControllerManager.current ?: return

    val isSystemInDarkTheme = isSystemInDarkTheme()


    val queueSheetState = rememberBottomSheetState(
        dismissedBound = QueuePeekHeight + WindowInsets.systemBars.asPaddingValues()
            .calculateBottomPadding(),
        expandedBound = state.expandedBound,
    )

    BottomSheet(
        state = state,
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
        onDismiss = { },
        collapsedContent = {
            MiniPlayer()
        }
    ) {
        SongScreen(
            navController = navController, viewModel = hiltViewModel<SongViewModel>()
        )
    }
}
