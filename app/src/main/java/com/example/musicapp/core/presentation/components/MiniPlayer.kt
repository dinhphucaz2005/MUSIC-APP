package com.example.musicapp.core.presentation.components

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.constants.MiniPlayerHeight
import com.example.musicapp.ui.theme.DarkGray
import com.example.musicapp.ui.theme.White
import com.example.musicapp.other.viewmodels.SongViewModel
import com.example.musicapp.util.MediaControllerManager


@Composable
fun MiniPlayer(
    state: BottomSheetState,
    mediaControllerManager: MediaControllerManager,
    songViewModel: SongViewModel
) {

    val currentSong by mediaControllerManager.currentSong.collectAsState()

    val playBackState by mediaControllerManager.playBackState.collectAsState()

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .fillMaxWidth()
            .background(DarkGray)
            .height(MiniPlayerHeight)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
                .clickable(enabled = true, onClick = state::expandSoft),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Thumbnail(
                Modifier
                    .clip(RoundedCornerShape(DefaultCornerSize))
                    .fillMaxHeight()
                    .aspectRatio(1f),
                thumbnailSource = currentSong.data.getThumbnail()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = currentSong.data.getSongTitle(),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            spacing = MarqueeSpacing.fractionOfContainer(1f / 10f)
                        ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp, color = White
                )
                Text(
                    text = currentSong.data.getSongArtist(),
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp, color = White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            IconButton(onClick = {
                mediaControllerManager.toggleLikedCurrentSong()
            }) {
                Icon(
                    imageVector = if (currentSong.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null, tint = White
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
                        mediaControllerManager.togglePlayPause()
                    }, tint = White
            )

            IconButton(onClick = { mediaControllerManager.playNextSong() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null, tint = White
                )
            }
        }
    }
}