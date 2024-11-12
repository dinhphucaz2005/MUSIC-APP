package com.example.musicapp.ui.screen.playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.ui.components.Thumbnail
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.PlayListViewModel

@Composable
fun PlayListItem(
    modifier: Modifier,
    playlist: PlayList = PlayList(),
    showDeleteButton: Boolean,
    viewModel: PlayListViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Thumbnail(
            thumbnailSource = playlist.thumbnailSource,
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
                text = playlist.name, color = MaterialTheme.colorScheme.primary,
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
            IconButton(onClick = { viewModel.deletePlayList(id = playlist.id) }) {
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
