package com.example.mymusicapp.ui.screen.song

import androidx.annotation.MainThread
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.mymusicapp.AppViewModel
import com.example.mymusicapp.R
import com.example.mymusicapp.di.FakeModule
import com.example.mymusicapp.enums.PlaylistState
import com.example.mymusicapp.ui.theme.MyBrush
import com.example.mymusicapp.ui.theme.myTextColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@UnstableApi
@Preview
@Composable
fun Preview() {
    SongScreen(
        viewModel = AppViewModel(
            FakeModule.provideSongFileRepository(),
            FakeModule.provideMediaControllerManager()
        )
    )
}

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongScreen(
    navController: NavHostController = NavHostController(LocalContext.current),
    viewModel: AppViewModel = hiltViewModel()
) {


    val isPlaying by viewModel.isPlaying().collectAsState()
    val bitmap by viewModel.getBitmap().collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.image2), contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxSize()
//                .blur(50.dp)
//        )
//        bitmap?.let {
//            Image(
//                bitmap = it, contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .blur(50.dp)
//            )
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(0.5f))
//        )

        val backButton = createRef()
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navController.popBackStack()
                }
                .constrainAs(backButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
            tint = Color(myTextColor)
        )

        val editButton = createRef()
        Icon(
            imageVector = Icons.Default.Edit, contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clickable {
                }
                .constrainAs(editButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            tint = Color(myTextColor)
        )

        val songTitle = createRef()

        Text(
            text = viewModel.getTitle().collectAsState().value,
            color = Color(myTextColor),
            maxLines = 1,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(songTitle) {
                    top.linkTo(backButton.bottom, margin = 16.dp)
                    start.linkTo(backButton.start)
                    end.linkTo(editButton.end)
                }
                .basicMarquee(
                    delayMillis = 0,
                    iterations = Int.MAX_VALUE,
                    spacing = MarqueeSpacing.fractionOfContainer(1f / 10f)
                )
        )
        val artist = createRef()

        Text(
            text = viewModel.getArtist().collectAsState().value,
            color = Color(myTextColor),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(artist) {
                top.linkTo(songTitle.bottom, margin = 12.dp)
                start.linkTo(songTitle.start)
                end.linkTo(songTitle.end)
            }
        )

        val image = createRef()

        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .background(
                    brush = MyBrush
                )
                .constrainAs(image) {
                    top.linkTo(artist.bottom)
                    start.linkTo(backButton.start)
                    end.linkTo(editButton.end)
                    width = Dimension.fillToConstraints
                }
                .aspectRatio(1f)
                .border(width = 10.dp, color = Color(myTextColor), shape = CircleShape)
        ) {
            bitmap?.let {
                Image(
                    bitmap = it, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        var sliderPosition by remember { mutableFloatStateOf(0f) }
        val (slider, row1, row2) = createRefs()
        LaunchedEffect(Unit) {
            while (true) {
                withContext(Dispatchers.Main) {
                    sliderPosition = viewModel.getCurrentPosition()
                }
                delay(1000)
            }
        }

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
            }, onValueChangeFinished = {
                viewModel.seekTo(sliderPosition)
            },
            colors = SliderDefaults.colors(
                thumbColor = Color(myTextColor),
                activeTrackColor = Color(myTextColor),
                inactiveTrackColor = Color(myTextColor).copy(0.5f)
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(slider) {
                    top.linkTo(image.bottom)
                    start.linkTo(backButton.start)
                    end.linkTo(editButton.end)
                    bottom.linkTo(row1.top)
                    width = Dimension.fillToConstraints
                }
        )

        val (startTime, endTime) = createRefs()
        val startText = "00:00"
        val endText = viewModel.getDuration().collectAsState().value
        Text(text = startText, modifier = Modifier.constrainAs(startTime) {
            top.linkTo(slider.bottom)
            bottom.linkTo(slider.bottom)
            start.linkTo(slider.start)
        }, color = Color(myTextColor), fontSize = 16.sp)
        Text(
            text = endText,
            modifier = Modifier.constrainAs(endTime) {
                top.linkTo(slider.bottom)
                bottom.linkTo(slider.bottom)
                end.linkTo(slider.end)
            },
            color = Color(myTextColor),
            fontSize = 16.sp
        )

        val size = 60.dp
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.constrainAs(row1) {
                top.linkTo(image.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            }
        ) {
            IconButton(
                onClick = { TODO("Not yet implemented") }, modifier = Modifier
                    .size(size)
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.rewind
                    ), contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }
            IconButton(
                onClick = { viewModel.playOrPause() }, modifier = Modifier
                    .size(size)
            ) {
                Icon(
                    painter = painterResource(
                        id = isPlaying.resource
                    ), contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }
            IconButton(
                onClick = { TODO("Not yet implemented") }, modifier = Modifier
                    .size(size)
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.fast_fwd
                    ), contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }
        }

        val playListState = viewModel.getPlayListState().collectAsState().value

        val iconSize = 40.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(iconSize)
                .constrainAs(row2) {
                    top.linkTo(row1.bottom)
                    bottom.linkTo(parent.bottom)
                },
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButton(
                onClick = { viewModel.updatePlayListState() },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    painter = painterResource(id = playListState.resource),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }

            IconButton(
                onClick = { viewModel.playBack() },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_back),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }


            IconButton(
                onClick = { viewModel.playNext() },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_fwd),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color(myTextColor)
                )
            }


//            Icon(
//                painter = painterResource(id = R.drawable.skip_fwd),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(iconSize)
//                    .clickable {
//                        viewModel.playNext()
//                    },
//                tint = Color(myTextColor)
//            )
//
//            Icon(
//                painter = painterResource(id = R.drawable.favourite),
//                contentDescription = null,
//                modifier = Modifier.size(iconSize),
//                tint = Color(myTextColor)
//            )
//
//            Icon(
//                painter = painterResource(id = R.drawable.music_list),
//                contentDescription = null,
//                modifier = Modifier.size(iconSize),
//                tint = Color(myTextColor)
//            )
        }
    }
}