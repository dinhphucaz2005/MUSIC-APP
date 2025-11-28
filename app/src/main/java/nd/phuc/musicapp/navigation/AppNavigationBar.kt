package nd.phuc.musicapp.navigation

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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.NavigationBarHeight
import nd.phuc.musicapp.R

@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<Screens>,
    backStack: NavBackStack<NavKey>,
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
                                backStack.add(screen)
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
        is Screens.PlaylistDetail -> R.string.playlist_detail
    }

@get:DrawableRes
private val Screens.iconId: Int
    get() = when (this) {
        Screens.Home -> R.drawable.ic_home
        Screens.Playlists -> R.drawable.ic_disc
        Screens.Library -> R.drawable.ic_disc
        is Screens.PlaylistDetail -> R.drawable.ic_disc
    }