package com.example.musicapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.viewmodels.MainViewModel
import com.example.musicapp.ui.theme.commonShape


@ExperimentalFoundationApi
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun SongPreview(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    showSongScreen: () -> Unit
) {
    val activeSong by viewModel.activeSong.collectAsState()
    val isCurrentlyPlaying by viewModel.isCurrentlyPlaying.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .height(72.dp)
            .clickable {
                showSongScreen()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val imageModifier = Modifier
            .clip(commonShape)
            .fillMaxHeight()
            .aspectRatio(1f)

        activeSong.smallBitmap?.let {
            Image(
                bitmap = it,
                modifier = imageModifier,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } ?: Image(
            painter = painterResource(id = R.drawable.image), contentDescription = null,
            modifier = imageModifier
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = activeSong.title,
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
                text = activeSong.author,
                modifier = Modifier
                    .padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
        Icon(
            painter = painterResource(
                isCurrentlyPlaying.resource
            ),
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
        IconButton(onClick = {
            viewModel.playNextTrack()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}