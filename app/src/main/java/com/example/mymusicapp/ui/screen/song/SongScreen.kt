package com.example.mymusicapp.ui.screen.song

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mymusicapp.R
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.MyBrush
import com.example.mymusicapp.ui.theme.TextColor
import com.example.mymusicapp.util.MediaControllerManager

@Preview
@Composable
fun SongScreen() {

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
                    .size(40.dp),
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
            var sliderPosition by remember { mutableFloatStateOf(0.5f) }
            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    MediaControllerManager.play(sliderPosition)
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
                    painter = painterResource(id = R.drawable.shuffle), contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Image(
                    painter = painterResource(id = R.drawable.skip_back), contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Image(
                    painter = painterResource(id = R.drawable.play), contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Image(
                    painter = painterResource(id = R.drawable.skip_fwd), contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Image(
                    painter = painterResource(id = R.drawable.favourite), contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}