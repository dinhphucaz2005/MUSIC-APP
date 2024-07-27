package com.example.mymusicapp.ui.screen.home

import androidx.annotation.OptIn
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.mymusicapp.R
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.ui.navigation.Routes
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.TextColor
import com.example.mymusicapp.util.MediaControllerManager


@ExperimentalMaterial3Api
@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = NavHostController(
        LocalContext.current
    ),
    viewModel: HomeViewModel = viewModel()
) {

    var query by remember {
        mutableStateOf("")
    }
    val songs = viewModel.songList

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(query = query, onQueryChange = {
            query = it
        }, onSearch = {
            viewModel.search(it)
        }, active = false, onActiveChange = {}) { }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(songs) { index, song ->
                SongItem(song = song) {
                    MediaControllerManager.playIndex(index)
                }
            }
        }
        SongPreview {
            navController.navigate(Routes.SONG)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song = Song(),
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
            song.smallBitmap?.let {
                Image(
                    bitmap = it, contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

        }
        Text(
            text = song.title ?: "Unknown",
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
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(0xFF77b2e0))
                .fillMaxHeight()
                .aspectRatio(1f)
        ) {
            currentSong.value.imageBitmap?.let {
                Image(
                    bitmap = it, contentDescription = null,
                    modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = currentSong.value.title ?: "Unknown",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onClick() },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextColor
            )
            Text(
                text = currentSong.value.artist ?: "Unknown",
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
                tint = IconTintColor,
            )
        }
        Icon(
            painter = painterResource(
                id = when (MediaControllerManager.isPlayingState.value) {
                    true -> R.drawable.pause
                    else -> R.drawable.play
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable {
                    MediaControllerManager.playOrPause()
                },
            tint = IconTintColor
        )
        IconButton(onClick = {
            MediaControllerManager.playNext()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = IconTintColor
            )
        }
    }
}