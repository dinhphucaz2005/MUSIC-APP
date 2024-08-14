package com.example.mymusicapp.ui.screen.playlist

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusicapp.domain.model.Playlist
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.R
import com.example.mymusicapp.callback.ResultCallback
import com.example.mymusicapp.domain.repository.PlaylistRepository
import com.example.mymusicapp.ui.theme.tempColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
@OptIn(UnstableApi::class)
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {

    val playlist by viewModel.playlistState.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (playlist.isNotEmpty()) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(tempColor)
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        modifier = Modifier.padding(vertical = 4.dp),
                        contentDescription = null
                    )
                    Text(text = "New playlist", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    ) { contentPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(tempColor)
                )
                .padding(paddingValues = contentPadding)
        ) {
            val showDialog = viewModel.showDialog
            if (showDialog.value) {
                MyCustomAlertDialog(viewModel = viewModel)
            }
            if (playlist.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(createRef()) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = "Playlists on your device will show up here",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(tempColor)
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddCircle,
                            modifier = Modifier.padding(vertical = 4.dp),
                            contentDescription = null
                        )
                        Text(text = "New playlist", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    itemsIndexed(playlist) { _, item ->
                        PlayListItem(item)
                    }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun MyCustomAlertDialog(viewModel: PlaylistViewModel) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = { Button(onClick = { viewModel.savePlaylist() }) { Text(text = "Confirm") } },
        dismissButton = { Button(onClick = { viewModel.offDialog() }) { Text(text = "Dismiss") } },
        title = { Text(text = "Title") },
        text = { Text(text = "Text") },
        containerColor = Color(tempColor)
    )
}

@Composable
fun PlayListItem(playList: Playlist = Playlist()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(tempColor)
            )
            .size(80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(Color(tempColor))
        ) {
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = playList.name, color = Color(tempColor),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
        Icon(
            imageVector = Icons.Default.PlayArrow, contentDescription = null,
            modifier = Modifier
                .padding(
                    vertical = 20.dp
                )
                .fillMaxHeight()
                .aspectRatio(1f),
            tint = Color(tempColor)
        )
    }
}
