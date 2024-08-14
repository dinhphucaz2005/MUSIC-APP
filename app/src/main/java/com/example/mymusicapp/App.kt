@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.mymusicapp

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymusicapp.ui.screen.home.HomeScreen
import com.example.mymusicapp.ui.screen.song.SongScreen
import com.example.mymusicapp.ui.theme.myTextColor

data class Temp(
    val painter: Painter,
    val text: String
)

@OptIn(UnstableApi::class)
@ExperimentalMaterial3Api
@Composable
fun App(appViewModel: AppViewModel) {

    val list = listOf(
        Temp(painterResource(id = R.drawable.ic_home), "Home"),
        Temp(painterResource(id = R.drawable.ic_library), "Library"),
        Temp(painterResource(id = R.drawable.icon), ""),
        Temp(painterResource(id = R.drawable.ic_store), "Store"),
        Temp(painterResource(id = R.drawable.ic_person), "Person"),
    )
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF0f1f36))
    ) {
        val bitmap by appViewModel.getBitmap().collectAsState()
        bitmap?.let {
            Image(
                bitmap = it, contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(50.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f))
        )

        val navController = rememberNavController()
        val bottomBar = createRef()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp)
                .constrainAs(bottomBar) {
                    bottom.linkTo(parent.bottom)
                }
        ) {
            for (i in 0..4) {
                if (i == 2) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = Color(myTextColor),
                                    shape = CircleShape
                                )

                        ) {

                            Image(
                                painter = list[i].painter, contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            bitmap?.let {
                                Image(
                                    bitmap = it, contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                        }

                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                navController.navigate(list[i].text)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = list[i].painter,
                            contentDescription = null,
                            tint = Color(myTextColor)
                        )
                        Text(
                            text = list[i].text,
                            color = Color(myTextColor)
                        )
                    }
                }

            }
        }

        val bottomDivider = createRef()
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomDivider) {
                    bottom.linkTo(bottomBar.top)
                }, thickness = 2.dp, color = Color(myTextColor)
        )

        NavHost(navController = navController,
            startDestination = list[0].text,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(createRef()) {
                    top.linkTo(parent.top, margin = 40.dp)
                    bottom.linkTo(bottomBar.top, margin = 2.dp)
                    height = Dimension.fillToConstraints
                }) {
            composable(list[0].text) {
                Text(text = "List[0]")
            }
            composable(list[1].text) {
                HomeScreen(modifier = Modifier.fillMaxSize(), viewModel = appViewModel)
            }
            composable(list[3].text) {
                SongScreen()
            }
            composable(list[4].text) {
                Text(text = "List[4]")
            }
        }
    }
}
