package com.example.musicapp.ui.screen.playlist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.ui.components.CommonImage
import com.example.musicapp.ui.screen.playlist.PlaylistViewModel
import com.example.musicapp.ui.theme.commonShape

@UnstableApi
@Composable
fun PlayListItem(
    modifier: Modifier,
    playList: Playlist = Playlist(),
    thumbnail: Painter,
    showDeleteButton: Boolean,
    viewModel: PlaylistViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommonImage(
            bitmap = playList.songs.firstOrNull()?.smallBitmap,
            painter = thumbnail,
            modifier = Modifier
                .clip(commonShape)
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.primary)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = playList.name, color = MaterialTheme.colorScheme.primary,
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
            tint = MaterialTheme.colorScheme.primary
        )
        if (showDeleteButton) {
            IconButton(onClick = { viewModel.deletePlaylist(playList.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete, contentDescription = null,
                    modifier = Modifier
                        .padding(
                            vertical = 20.dp
                        )
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
