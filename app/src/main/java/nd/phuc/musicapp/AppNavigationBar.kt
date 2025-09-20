package nd.phuc.musicapp

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.NavigationBarHeight

@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<Screens>,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .height(NavigationBarHeight), verticalAlignment = Alignment.CenterVertically
    ) {
        navigationItems.forEachIndexed { index, screen ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                if (screen == Screens.Home) {
                                    navController.navigate(Screens.Home.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else {
                                    navController.navigate(screen.route) {
                                        popUpTo(Screens.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        },
                    ),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val color = MaterialTheme.colorScheme.primary
                Icon(
                    painter = painterResource(screen.iconId),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null,
                    tint = color,
                )
                Text(
                    text = stringResource(screen.titleId),
                    color = color,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                )
            }
        }
    }

}

@get:StringRes
private val Screens.titleId: Int
    get() = when (this) {
        Screens.Home -> R.string.home
        Screens.Playlists -> R.string.playlists
        Screens.Library -> R.string.library
    }

@get:DrawableRes
private val Screens.iconId: Int
    get() = when (this) {
        Screens.Home -> R.drawable.ic_home
        Screens.Playlists -> R.drawable.ic_disc
        Screens.Library -> R.drawable.ic_disc
    }