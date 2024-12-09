package com.example.musicapp.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.musicapp.constants.BottomBarHeight
import com.example.musicapp.Item
import com.example.musicapp.other.presentation.ui.navigation.Routes

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    list: List<Item>,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(BottomBarHeight)
    ) {
        list.forEachIndexed { _, item ->
            RegularNavigationBarItem(item = item) {
                if (!navController.currentDestination?.route.toString()
                        .startsWith(item.route)
                ) {
                    if (item.route == Routes.HOME.name)
                        navController.popBackStack()
                    else
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
fun RowScope.RegularNavigationBarItem(
    modifier: Modifier = Modifier,
    item: Item,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = item.painter,
            modifier = Modifier.size(24.dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = item.route,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
        )
    }
}


