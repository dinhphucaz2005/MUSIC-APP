package com.example.musicapp.ui.components

import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.ui.theme.commonShape
import com.example.musicapp.viewmodels.MainViewModel
import kotlinx.coroutines.delay


@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val playBackState by viewModel.playBackState.collectAsState()
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            sliderPosition = viewModel.getCurrentSliderPosition()
            delay(100)
        }
    }

    Column {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .height(MiniPlayerHeight)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Thumbnail(
                Modifier
                    .clip(commonShape)
                    .fillMaxHeight()
                    .aspectRatio(1f),
                currentSong.thumbnail
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = currentSong.title,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            spacing = MarqueeSpacing.fractionOfContainer(1f / 10f)
                        ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = currentSong.author,
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            IconButton(onClick = { TODO("Add song to favourite") }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            }
            Icon(
                painter = painterResource(playBackState.playerState.resource),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clickable {
                        viewModel.togglePlayback()
                    },
                tint = MaterialTheme.colorScheme.onSecondary
            )
            IconButton(onClick = { viewModel.playNextTrack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        LinearProgressIndicator(
            progress = { sliderPosition }, modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
        )
    }
}
