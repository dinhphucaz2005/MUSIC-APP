package com.example.mymusicapp.ui.screen

import android.annotation.SuppressLint
import android.speech.ModelDownloadListener
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.mymusicapp.R
import com.example.mymusicapp.presentation.viewmodel.MainViewModel
import com.example.mymusicapp.ui.navigation.Routes
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.TextColor
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymusicapp.ui.screen.home.HomeScreen

@SuppressLint("RememberReturnType")
@Preview
@Composable
fun App(
    viewModel: MainViewModel = viewModel()
) {
    var isSongPreviewGone by remember {
        mutableStateOf(false)
    }

    val songs = viewModel.songList

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Background
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isSongPreviewGone = !isSongPreviewGone }) {
                    Icon(
                        imageVector = Icons.Default.Menu, contentDescription = null,
                        tint = IconTintColor,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    text = "My Music App", fontSize = 26.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = TextColor
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = null,
                        tint = IconTintColor,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(Background)
                    .fillMaxWidth()
                    .padding(
                        8.dp
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate(Routes.HOME)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.music_list),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate(Routes.SONG)
                        }
                )
            }
        }
    ) { contentPadding ->
        HomeScreen(
            songs,
            Modifier
                .background(Background)
                .padding(contentPadding)
        )
    }

}