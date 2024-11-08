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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dragselectcompose.grid.indicator.internal.RadioButtonUnchecked
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.ui.components.CommonIcon
import com.example.musicapp.ui.components.CommonImage
import com.example.musicapp.ui.screen.song.MyTextField
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.EditPlayListViewModel

@UnstableApi
@Preview
@Composable
fun PreviewScreen() {
    MusicTheme {
        PlaylistEdit(
            0, NavHostController(LocalContext.current), FakeModule.provideSelectSongViewModel()
        )
    }
}

@Composable
fun PlaylistEdit(
    id: Long,
    navController: NavController,
    viewModel: EditPlayListViewModel = hiltViewModel()
) {

    LaunchedEffect(id) {
        viewModel.loadPlaylist(id)
    }

    val itemModifier = Modifier
        .fillMaxWidth()
        .height(80.dp)

    val name by viewModel.name.collectAsState()
    val localSongs by viewModel.localSongs.collectAsState()
    val selectedSongs = remember { mutableStateListOf<Long>() }

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

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            MyTextField(
                value = name,
                label = "Playlist Name",
                onValueChange = { viewModel.updatePlayListName(it) },
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = {
                viewModel.savePlaylist(name, selectedSongs)
                navController.popBackStack()
            }) { CommonIcon(painter = painterResource(id = R.drawable.ic_save)) }
        }

        val painter = painterResource(R.drawable.image)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(songListRef) {
                    top.linkTo(editTextRef.bottom, margin = 12.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp)
        ) {
            itemsIndexed(localSongs.songs, key = { _, item -> item.id }) { _, item ->
                val isSelected = selectedSongs.contains(item.id)
                SelectableSongItem(
                    itemModifier.clickable {
                        if (isSelected)
                            selectedSongs.remove(item.id)
                        else
                            selectedSongs.add(item.id)
                    }, item, painter, isSelected
                )
            }
        }
    }
}


@SuppressLint("PrivateResource")
@UnstableApi
@Composable
fun SelectableSongItem(
    modifier: Modifier = Modifier,
    song: Song,
    thumbnail: Painter,
    isSelected: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .clip(commonShape)
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxHeight()
            .aspectRatio(1f)

        CommonImage(bitmap = song.thumbnail, painter = thumbnail, imageModifier)


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
            tint = MaterialTheme.colorScheme.primary, contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
        )
    }
}
