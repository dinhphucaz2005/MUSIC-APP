package com.example.musicapp.ui

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.R
import com.example.musicapp.ui.components.SongPreview
import com.example.musicapp.ui.screen.home.HomeScreen
import com.example.musicapp.ui.screen.setting.SettingScreen
import com.example.musicapp.ui.screen.song.SongScreen

data class Item(
    val painter: Painter,
    val route: String
)

@OptIn(UnstableApi::class)
@ExperimentalMaterial3Api
@Composable
fun App(appViewModel: AppViewModel) {

    val list = listOf(
        Item(painterResource(id = R.drawable.ic_home), "Home"),
        Item(painterResource(id = R.drawable.ic_library), "Library"),
        Item(painterResource(id = R.drawable.ic_store), "Store"),
        Item(painterResource(id = R.drawable.ic_person), "Person"),
    )
    val navController = rememberNavController()

    var showSongScreen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController,
                appViewModel = appViewModel,
                list = list,
            ) { showSongScreen = true }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = list[0].route,
            modifier = Modifier
                .padding(paddingValues = contentPadding)
                .fillMaxSize()
        ) {
            composable(list[0].route) {
                HomeScreen(viewModel = appViewModel)
            }
            composable(list[1].route) {
//                PlaylistScreen()
                SongScreen(
                    onDismiss = { /*TODO*/ },
                    viewModel = appViewModel,
                    editViewModel = hiltViewModel()
                )
            }
            composable(list[2].route) {
            }
            composable(list[3].route) {
                SettingScreen(appViewModel)
            }
        }
    }


    AnimatedVisibility(
        visible = showSongScreen,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(
                durationMillis = 600,
                easing = EaseInOut
            )
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = 600,
                easing = EaseInOut
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(
                durationMillis = 600,
                easing = EaseInOut
            )
        ) + scaleOut(
            animationSpec = tween(
                durationMillis = 600,
                easing = EaseInOut
            )
        )
    ) {
        SongScreen(
            onDismiss = { showSongScreen = false },
            viewModel = appViewModel,
            editViewModel = hiltViewModel(),
            modifier = Modifier.clickable(enabled = false, onClick = {})
        )
    }
}

@UnstableApi
@Composable
fun BottomBar(
    navController: NavController,
    appViewModel: AppViewModel,
    list: List<Item>,
    showSongScreen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            SongPreview(viewModel = appViewModel) { showSongScreen() }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                list.forEachIndexed { _, item ->
                    RegularBottomBarItem(Modifier.weight(1f), item) {
                        navController.navigate(item.route)
                    }
                }
            }
        }
    }
}

@Composable
fun RegularBottomBarItem(modifier: Modifier, item: Item, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = item.painter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = item.route,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


