package com.example.musicapp.ui.screen.playlist

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.ui.components.CommonIcon
import com.example.musicapp.ui.components.Thumbnail
import com.example.musicapp.ui.components.MyTextField
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.PlayListViewModel

@UnstableApi
@Preview
@Composable
fun PreviewScreen() {
    MusicTheme {
        PlayListEdit(
            rememberNavController(), FakeModule.providePlaylistViewModel()
        )
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun PlayListEdit(
    navController: NavController, viewModel: PlayListViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.loadLocalSongs()
    }

    val itemModifier = Modifier
        .fillMaxWidth()
        .height(80.dp)

    val isLoading by viewModel.isLoading.collectAsState()
    val activePlayList by viewModel.activePlayList.collectAsState()
    val localSongs by viewModel.localSongs.collectAsState()
    val selectedSongs = remember { mutableStateListOf<String>() }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            ) { navController.popBackStack() }

            MyTextField(
                value = activePlayList?.name ?: "",
                label = "Playlist Name",
                onValueChange = { viewModel.updatePlayListName(it) },
                modifier = Modifier.weight(1f)
            )

            CommonIcon(
                icon = R.drawable.ic_save
            ) {
                viewModel.savePlaylist(activePlayList, selectedSongs.toList())
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
                    val isSelected = selectedSongs.contains(item.id)
                    SelectableSongItem(
                        itemModifier.clickable {
                            if (isSelected) selectedSongs.remove(item.id)
                            else selectedSongs.add(item.id)
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
    modifier: Modifier = Modifier, song: Song, isSelected: Boolean = false
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .clip(commonShape)
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
