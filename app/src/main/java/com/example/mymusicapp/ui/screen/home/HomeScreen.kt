package com.example.mymusicapp.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Index
import coil.compose.AsyncImage
import com.example.mymusicapp.R
import com.example.mymusicapp.data.dto.SongFileDTO
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.ui.screen.song.SongScreen
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.TextColor
import com.example.mymusicapp.util.MediaControllerManager


@Preview
@Composable
fun HomeScreen(songList: List<Song> = emptyList(), modifier: Modifier = Modifier) {

    val isPlaying = true

    var isSongScreenVisible by remember {
        mutableStateOf(false)
    }

    if (isSongScreenVisible) {
        SongScreen()
    } else

        Column(
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(songList) { index, song ->
                    SongItem(song) {
                        MediaControllerManager.playIndex(index)
                    }
                }
            }
            if (isPlaying)
                SongPreview {
                    isSongScreenVisible = true
                }
        }
}

@Preview(showBackground = true)
@Composable
fun SongItem(
    song: Song = Song("NO SONG FOUND", null, null, "NO ARTIST FOUND"),
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier
            .background(Background)
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(
                    color = Color(0xFFf2dadf)
                )
        ) {
            if (song.thumbnail != null) {
                Image(
                    bitmap = song.thumbnail.asImageBitmap(), contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

        }

        Text(
            text = song.title.toString(),
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .weight(1f)
                .padding(start = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextColor,
        )
    }
}

@Preview
@Composable
fun SongPreview(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {

    val currentSong = MediaControllerManager.currentSong

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .background(TextColor.copy(0.5f))
            .height(72.dp)
            .padding(8.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = currentSong.value.thumbnail,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = currentSong.value.title ?: "NO SONG FOUND",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onClick() },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextColor
            )
            Text(
                text = currentSong.value.artist ?: "NO ARTIST FOUND",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onClick() },
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextColor
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder, contentDescription = null,
                tint = IconTintColor
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.play),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight()
                .aspectRatio(1f),
            tint = IconTintColor
        )
        IconButton(onClick = {
            MediaControllerManager.playNext()
        }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null,
                tint = IconTintColor
            )
        }
    }
}