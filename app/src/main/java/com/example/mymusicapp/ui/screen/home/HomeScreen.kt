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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.AppViewModel
import com.example.mymusicapp.R
import com.example.mymusicapp.di.FakeModule
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.ui.theme.myBackgroundColor
import com.example.mymusicapp.ui.theme.myTextColor


@Preview
@ExperimentalMaterial3Api
@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = FakeModule.provideViewModel()
) {
    val songs = viewModel.songList
    val playingState by viewModel.isPlaying().collectAsState()
    val bitmap by viewModel.getBitmap().collectAsState()

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val iconSize = 32.dp

        val backButton = createRef()
        Icon(
            painter = painterResource(id = R.drawable.back), contentDescription = null,
            tint = Color(myTextColor),
            modifier = Modifier
                .constrainAs(backButton) {
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(parent.start, margin = 12.dp)
                }
                .size(iconSize)
        )

        val settingButton = createRef()
        Icon(
            painter = painterResource(id = R.drawable.home), contentDescription = null,
            tint = Color(myTextColor),
            modifier = Modifier
                .constrainAs(settingButton) {
                    top.linkTo(parent.top, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                }
                .size(iconSize)
        )

        val image = createRef()
        Box(modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(myBackgroundColor))
            .constrainAs(image) {
                top.linkTo(backButton.bottom, margin = 16.dp)
                start.linkTo(backButton.start)
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon), contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(myTextColor))
                    .fillMaxSize()
            )
            bitmap?.let {
                Image(
                    contentScale = ContentScale.Crop,
                    bitmap = it, contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(myBackgroundColor))
                        .fillMaxSize()
                )
            }
        }


        val textRef = createRef()

        Column(
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                    start.linkTo(image.end)
                    end.linkTo(settingButton.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = viewModel.getTitle().collectAsState().value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                softWrap = true,
                color = Color(myTextColor),
            )
            Text(
                text = viewModel.getArtist().collectAsState().value,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color(myTextColor),
            )
        }

        val row = createRef()

        Row(
            modifier = Modifier
                .constrainAs(row) {
                    top.linkTo(image.bottom)
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val list = listOf(
                painterResource(id = R.drawable.repeat),
                painterResource(id = R.drawable.skip_back),
                painterResource(id = R.drawable.pause),
                painterResource(id = R.drawable.skip_fwd),
                painterResource(id = R.drawable.play)
            )

            val playListState by viewModel.getPlayListState().collectAsState()
            IconButton(onClick = { viewModel.updatePlayListState() }) {
                Icon(
                    painter = painterResource(id = playListState.resource),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .padding(8.dp),
                    tint = Color(myTextColor)
                )
            }
            IconButton(onClick = { viewModel.playBack() }) {
                Icon(
                    painter = list[1], contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .padding(8.dp),
                    tint = Color(myTextColor)
                )
            }
            IconButton(onClick = { viewModel.playOrPause() }) {
                Icon(
                    painter = painterResource(id = playingState.resource),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .padding(8.dp),
                    tint = Color(myTextColor)
                )
            }
            IconButton(onClick = { viewModel.playNext() }) {
                Icon(
                    painter = list[3], contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .padding(8.dp),
                    tint = Color(myTextColor)
                )
            }
        }

        val divider = createRef()
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.constrainAs(divider) {
            top.linkTo(row.bottom)
        }, color = Color(myTextColor))

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .constrainAs(createRef()) {
                    top.linkTo(divider.bottom, margin = 12.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
        ) {
            itemsIndexed(songs) { index, song ->
                SongItem(song = song, index = index, viewModel = viewModel)
            }
        }
    }
}

@Preview
@UnstableApi
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song = Song(),
    index: Int = 0,
    viewModel: AppViewModel = FakeModule.provideViewModel()
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable { viewModel.playIndex(index) }
            .height(80.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (song.imageBitmap != null) {
            Image(
                bitmap = song.imageBitmap, contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.icon), contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(myBackgroundColor))
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = song.fileName,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(myTextColor),
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = null,
                tint = Color(myTextColor),
            )
        }
    }
}