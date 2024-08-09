package com.example.mymusicapp.ui.screen.playlist

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mymusicapp.domain.model.Playlist
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.TextColor
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController

@Composable
@OptIn(UnstableApi::class)
fun PlaylistScreen(
    viewModel: PlaylistViewModel = viewModel(),
) {
    Scaffold(
        floatingActionButton = {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = IconTintColor
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
    ) { contentPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Background
                )
                .padding(paddingValues = contentPadding)
        ) {
            if (viewModel.playlistState.isEmpty()) {
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
                        imageVector = Icons.Filled.List,
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
                            containerColor = IconTintColor
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
                    itemsIndexed(viewModel.playlistState) { _, item ->
                        PlayListItem(item)
                    }
                }
            }
        }
    }

}

@Composable
fun PlayListItem(playList: Playlist = Playlist()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Background
            )
            .size(80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(IconTintColor)
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
                text = playList.name, color = TextColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Text(
                text = "${playList.songs.size} songs", color = TextColor,
                fontWeight = FontWeight.Bold, fontSize = 16.sp
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
            tint = IconTintColor
        )
    }
}