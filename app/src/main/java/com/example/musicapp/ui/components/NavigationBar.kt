package com.example.musicapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.musicapp.constants.NavigationBarHeight
import com.example.musicapp.ui.Item
import com.example.musicapp.ui.navigation.Routes

@ExperimentalFoundationApi
@UnstableApi
@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    list: List<Item>,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(NavigationBarHeight)
    ) {
        list.forEachIndexed { _, item ->
            RegularNavigationBarItem(Modifier.weight(1f), item) {
                if (!navController.currentDestination?.route.toString()
                        .startsWith(item.route)
                ) {
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

@Composable
fun RegularNavigationBarItem(modifier: Modifier, item: Item, onClick: () -> Unit) {
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


