package com.example.mymusicapp.ui.screen.song

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mymusicapp.R
import com.example.mymusicapp.enums.PlaylistState
import com.example.mymusicapp.ui.navigation.Routes
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.MyBrush
import com.example.mymusicapp.ui.theme.MyMusicAppTheme
import com.example.mymusicapp.ui.theme.TextColor
import com.example.mymusicapp.util.MediaControllerManager
import kotlinx.coroutines.delay

@Preview(showSystemUi = true)
@Composable
fun SongScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
) {

    val song = MediaControllerManager.currentSong
    MyMusicAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.popBackStack()
                        },
                    tint = IconTintColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Edit, contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate(Routes.EDIT_SONG)
                        },
                    tint = IconTintColor
                )
            }



            Column(
                modifier = Modifier
                    .background(Background)
                    .fillMaxHeight()
                    .padding(top = 12.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            val width = minOf(placeable.width, placeable.height)
                            val newConstraints =
                                constraints.copy(
                                    minHeight = width, maxHeight = width,
                                    minWidth = width, maxWidth = width
                                )
                            val newPlaceable = measurable.measure(newConstraints)
                            layout(newPlaceable.width, newPlaceable.height) {
                                newPlaceable.place(0, 0)
                            }
                        }
                        .background(
                            brush = MyBrush
                        )
                ) {
                    song.value.imageBitmap?.let {
                        Image(
                            bitmap = it, contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Text(
                    text = song.value.title ?: "Unknown",
                    color = TextColor,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = song.value.artist ?: "Unknown",
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
                    Icon(
                        painter = painterResource(
                            id = when (MediaControllerManager.playListState.value) {
                                PlaylistState.SHUFFLE -> R.drawable.shuffle
                                PlaylistState.REPEAT_ALL -> R.drawable.repeat
                                else -> R.drawable.repeat_one
                            }
                        ), contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .clickable {
                                MediaControllerManager.changePlayListState()
                            },
                        tint = IconTintColor
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.skip_back),
                        contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .clickable {
                                MediaControllerManager.playPrevious()
                            },
                        tint = IconTintColor
                    )

                    Icon(
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
                            },
                        tint = IconTintColor
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.skip_fwd),
                        contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .clickable {
                                MediaControllerManager.playNext()
                            },
                        tint = IconTintColor
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.favourite),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = IconTintColor
                    )
                }
            }


        }
    }
}