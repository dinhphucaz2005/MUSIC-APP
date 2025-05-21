package com.example.musicapp.music.presentation.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.musicapp.core.presentation.previews.ExtendDevicePreviews
import com.example.musicapp.ui.theme.MyMusicAppTheme

@ExtendDevicePreviews
@Composable
private fun YourPlaylistsSectionPreview() {
    MyMusicAppTheme {
        YourPlaylistsSection()
    }
}

@Composable
fun YourPlaylistsSection() {

    Column {
        Text(
            text = "Your Playlists",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(4) { index ->
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = "Playlist ${index + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}
