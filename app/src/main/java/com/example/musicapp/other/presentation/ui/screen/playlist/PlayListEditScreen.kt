package com.example.musicapp.other.presentation.ui.screen.playlist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dragselectcompose.grid.indicator.internal.RadioButtonUnchecked
import com.example.musicapp.R
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.core.presentation.components.CommonIcon
import com.example.musicapp.core.presentation.components.LoadingScreen
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.core.presentation.components.MyTextField
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.other.viewmodels.HomeViewModel
import com.example.musicapp.other.viewmodels.PlaylistViewModel

@UnstableApi
@Preview
@Composable
fun PreviewScreen() {
    MusicTheme {
        PlayListEdit(
            "playlistId",
            rememberNavController(),
            FakeModule.providePlaylistViewModel(),
            FakeModule.provideHomeViewModel()
        )
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun PlayListEdit(
    playlistId: String,
    navController: NavController,
    viewModel: PlaylistViewModel,
    homeViewModel: HomeViewModel,
) {

    val itemModifier = Modifier
        .fillMaxWidth()
        .height(80.dp)

    var playlistName by remember { mutableStateOf(viewModel.getPlaylistName(playlistId)) }

    val isLoading by viewModel.isLoading.collectAsState()
    val localSongs by homeViewModel.songs.collectAsState()
    val selectedSongIds = remember { mutableStateListOf<String>() }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        val (editTextRef, songListRef) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(editTextRef) {
                    top.linkTo(parent.top)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CommonIcon(
                icon = R.drawable.ic_back,
                onClick = navController::popBackStack
            )

            MyTextField(
                value = playlistName,
                label = "Playlist Name",
                onValueChange = { playlistName = it },
                modifier = Modifier.weight(1f)
            )

            CommonIcon(
                icon = R.drawable.ic_save
            ) {
                viewModel.savePlaylist(
                    playlistId,
                    playlistName,
                    localSongs.filter { selectedSongIds.contains(it.id) }
                )
                navController.popBackStack()
            }
        }

        val contentModifier = Modifier
            .fillMaxWidth()
            .constrainAs(songListRef) {
                top.linkTo(editTextRef.bottom, margin = 12.dp)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }

        if (isLoading) LoadingScreen(contentModifier)
        else {
            LazyColumn(
                modifier = contentModifier,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp)
            ) {
                itemsIndexed(localSongs, key = { _, item -> item.id }) { _, item ->
                    val isSelected = selectedSongIds.contains(item.id)
                    SelectableSongItem(
                        itemModifier.clickable {
                            if (isSelected) selectedSongIds.remove(item.id)
                            else selectedSongIds.add(item.id)
                        }, item, isSelected
                    )
                }
            }
        }
    }
}


@UnstableApi
@Composable
fun SelectableSongItem(
    modifier: Modifier = Modifier, song: Song, isSelected: Boolean = false,
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .clip(RoundedCornerShape(DefaultCornerSize))
            .fillMaxHeight()
            .aspectRatio(1f)

        Thumbnail(thumbnailSource = song.thumbnailSource, modifier = imageModifier)

        Text(
            text = song.title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Icon(
            imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
            modifier = Modifier.padding(6.dp)
        )
    }
}
