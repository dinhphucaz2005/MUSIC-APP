package com.example.mymusicapp.ui.screen.song

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mymusicapp.R
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.MyBrush
import com.example.mymusicapp.ui.theme.TextColor
import com.example.mymusicapp.util.MediaControllerManager
import com.example.mymusicapp.util.PlayListState
import kotlinx.coroutines.delay

@Preview
@Composable
fun SongScreen(
    navController: NavHostController = NavHostController(LocalContext.current)
) {

    val song = MediaControllerManager.currentSong

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back), contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        navController.popBackStack()
                    },
                tint = IconTintColor
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.settings), contentDescription = null,
                modifier = Modifier
                    .size(40.dp),
                tint = IconTintColor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        brush = MyBrush
                    )
            ) {
                song.value.thumbnail?.let {
                    Image(
                        bitmap = it.asImageBitmap(), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Text(
                text = song.value.title.toString(),
                color = TextColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = song.value.artist.toString(),
                color = TextColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            var sliderPosition by remember { mutableFloatStateOf(0f) }
            LaunchedEffect(Unit) {
                while (true) {
                    sliderPosition = MediaControllerManager.getCurrentPosition()
                    delay(1000L)
                }
            }

            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    MediaControllerManager.seekTo(sliderPosition)
                },
                colors = SliderDefaults.colors(
                    thumbColor = TextColor,
                    activeTrackColor = TextColor,
                    inactiveTrackColor = TextColor.copy(0.5f)
                ),
                modifier = Modifier.padding(top = 16.dp)
            )

            val iconSize = 40.dp

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(
                        id = when (MediaControllerManager.playListState.value) {
                            PlayListState.SHUFFLE -> R.drawable.shuffle
                            PlayListState.REPEAT_ALL -> R.drawable.repeat
                            else -> R.drawable.repeat_one
                        }
                    ), contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(iconSize)
                        .clickable {
                            MediaControllerManager.changePlayListState()
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.skip_back), contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(iconSize)
                        .clickable {
                            MediaControllerManager.playPrevious()
                        }
                )

                Image(
                    painter = painterResource(
                        id = when (MediaControllerManager.isPlayingState.value) {
                            true -> R.drawable.pause
                            else -> R.drawable.play
                        }
                    ), contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .clickable {
                            MediaControllerManager.playOrPause()
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.skip_fwd), contentDescription = null,
                    modifier = Modifier
                        .size(iconSize)
                        .clickable {
                            MediaControllerManager.playNext()
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.favourite), contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}