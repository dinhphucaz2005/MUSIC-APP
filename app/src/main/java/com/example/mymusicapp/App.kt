package com.example.mymusicapp

import android.annotation.SuppressLint
import androidx.annotation.OptIn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymusicapp.ui.navigation.Routes
import com.example.mymusicapp.ui.screen.edit.EditScreen
import com.example.mymusicapp.ui.screen.home.HomeScreen
import com.example.mymusicapp.ui.screen.song.SongScreen
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor
import com.example.mymusicapp.ui.theme.TextColor

@OptIn(UnstableApi::class)
@SuppressLint("RememberReturnType")
@Preview
@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
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
                        IconButton(onClick = { }) {
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
                                    navController.navigate(Routes.PLAYLIST)
                                }
                        )
                    }
                }
            ) { contentPadding ->
                HomeScreen(
                    Modifier
                        .background(Background)
                        .padding(contentPadding),
                    navController
                )
            }
        }
        composable(Routes.SONG) {
            SongScreen(navController)
        }
        composable("${Routes.EDIT_SONG}/{songUri}") { backStackEntry ->
            val songUri = backStackEntry.arguments?.getString("songUri")
            if (songUri != null) {
                EditScreen(navController = navController, songUri = songUri)
            } else {
                navController.popBackStack()
            }
        }
    }
}

