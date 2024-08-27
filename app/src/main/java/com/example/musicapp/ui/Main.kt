@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.musicapp.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.R
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.components.BottomBar
import com.example.musicapp.ui.navigation.Routes
import com.example.musicapp.ui.navigation.playlist
import com.example.musicapp.ui.screen.home.HomeScreen
import com.example.musicapp.ui.screen.theme.ThemeScreen
import com.example.musicapp.ui.theme.MusicTheme

data class Item(
    val painter: Painter,
    val route: String
)


@UnstableApi
@Preview(showBackground = true)
@Composable
fun Preview() {
    MusicTheme {
        Main(FakeModule.provideViewModel()) {
        }
    }
}

@OptIn(UnstableApi::class)
@ExperimentalMaterial3Api
@Composable
fun Main(mainViewModel: MainViewModel, showSongScreen: () -> Unit) {

    val list = listOf(
        Item(painterResource(id = R.drawable.ic_home), Routes.HOME.name),
        Item(painterResource(id = R.drawable.ic_disc), Routes.PLAYLIST.name),
        Item(painterResource(id = R.drawable.ic_person), Routes.PERSON.name),
        Item(painterResource(id = R.drawable.ic_setting), Routes.SETTING.name),
    )

    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController,
                mainViewModel = mainViewModel,
                list = list,
            ) { showSongScreen() }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = list[0].route,
            modifier = Modifier
                .padding(paddingValues = contentPadding)
                .fillMaxSize()
        ) {
            composable(Routes.HOME.name) { HomeScreen(viewModel = mainViewModel) }
            playlist(navController)
            composable(Routes.PERSON.name) { ThemeScreen() }
            composable(Routes.SETTING.name) { Text(text = "Setting") }
        }
    }
}