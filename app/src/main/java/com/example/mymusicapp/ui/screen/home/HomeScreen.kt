package com.example.mymusicapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mymusicapp.R
import com.example.mymusicapp.data.dto.SongFileDTO
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.TextColor


@Preview
@Composable
fun HomeScreen(songList: List<Song> = emptyList(), modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(songList) { index, song ->
                SongItem(song)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongItem(song: Song = Song("", null, null), modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(Background)
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = song.thumbnail, contentDescription = null,
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
                text = song.title.toString(), modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextColor
            )
//            Text(
//                text = "song.getPath()", modifier = Modifier.padding(start = 8.dp),
//                fontWeight = FontWeight.Bold,
//                fontSize = 12.sp,
//                color = TextColor
//            )
        }
    }
}

@Preview
@Composable
fun SongPreview(songFileDTO: SongFileDTO = SongFileDTO(), modifier: Modifier = Modifier) {
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
            model = songFileDTO.getThumbnail(),
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
                text = songFileDTO.getTitle(), modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextColor
            )
            Text(
                text = songFileDTO.getPath(), modifier = Modifier.padding(start = 8.dp),
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
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null,
                tint = IconTintColor
            )
        }
    }
}