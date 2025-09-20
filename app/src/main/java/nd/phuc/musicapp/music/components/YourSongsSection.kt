package nd.phuc.musicapp.music.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nd.phuc.core.domain.model.Song
import nd.phuc.core.presentation.components.LazyColumnWithAnimation2
import nd.phuc.core.presentation.components.SongItemContent
import nd.phuc.core.presentation.previews.ExtendDevicePreviews
import nd.phuc.core.presentation.theme.MyMusicAppTheme
import nd.phuc.musicapp.LocalMediaControllerManager
import nd.phuc.musicapp.LocalMenuState

@ExtendDevicePreviews
@Composable
private fun YourSongsSectionPreview() {
    MyMusicAppTheme {
        Box(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
        ) {
            YourSongsSection(
                songs = listOf(
                ),
            )
        }
    }
}


@Composable
fun YourSongsSection(
    songs: List<Song>,
    modifier: Modifier = Modifier,
) {

    val mediaControllerManager = LocalMediaControllerManager.current
    val menuState = LocalMenuState.current

    LazyColumnWithAnimation2(
        items = songs,
        modifier = modifier
    ) { itemModifier, index, item ->
        SongItemContent(
            song = item,
            modifier = itemModifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            onSongClick = { mediaControllerManager.play(item) },
            onMoreChoice = {
                menuState.show {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        )
    }
}