package com.example.musicapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.musicapp.ui.MainViewModel
import com.example.musicapp.ui.Item
import com.example.musicapp.ui.navigation.Routes

@ExperimentalFoundationApi
@UnstableApi
@Composable
fun BottomBar(
    navController: NavController,
    mainViewModel: MainViewModel,
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
            SongPreview(viewModel = mainViewModel) { showSongScreen() }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                list.forEachIndexed { _, item ->
                    RegularBottomBarItem(Modifier.weight(1f), item) {
                        println("${navController.currentDestination?.route}")
                        if (!navController.currentDestination?.route.toString()
                                .startsWith(item.route)
                        ) {
                            println("${navController.currentDestination?.route}")
                            navController.navigate(item.route) {
                                popUpTo(Routes.HOME.name) {
                                    inclusive = (item.route == Routes.HOME.name)
                                }
                            }
                        }
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


