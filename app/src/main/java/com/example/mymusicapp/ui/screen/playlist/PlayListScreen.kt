package com.example.mymusicapp.ui.screen.playlist

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusicapp.domain.model.PlayList
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.TextColor

@Preview
@Composable
fun PlayListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Background
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(10) {
                PlayListItem(playList = PlayList())
            }
        }
    }
}

@Preview
@Composable
fun PlayListItem(playList: PlayList = PlayList()) {
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
            playList.imageBitmap?.let {
                Image(
                    bitmap = it, contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
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