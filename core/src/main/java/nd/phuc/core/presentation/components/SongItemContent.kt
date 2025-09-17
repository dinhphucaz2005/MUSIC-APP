package nd.phuc.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nd.phuc.core.model.DefaultCornerSize
import nd.phuc.core.model.Song

//import nd.phuc.musicapp.constants.DefaultCornerSize
//import nd.phuc.musicapp.di.FakeModule
//import nd.phuc.core.model.Song
//import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
//
//@Preview
//@Composable
//private fun SongItemContentPreview() {
//    MyMusicAppTheme {
//        Column {
//            MiniSongItemContent(FakeModule.localSong)
//            SongItemContent(
//                song = FakeModule.localSong,
//                onSongClick = {}
//            ) { }
//        }
//    }
//}
//
//
//@Composable
//fun MiniSongItemContent(song: Song) {
//    MyListItem(
//        headlineContent = {
//            Text(
//                text = song.getSongTitle(),
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.primary,
//                maxLines = 1,
//                modifier = Modifier.basicMarquee(
//                    iterations = Int.MAX_VALUE, spacing = MarqueeSpacing.fractionOfContainer(
//                        1f / 10f
//                    )
//                )
//            )
//        }, leadingContent = {
//            Thumbnail(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .clip(RoundedCornerShape(DefaultCornerSize))
//                    .fillMaxHeight()
//                    .aspectRatio(1f), thumbnailSource = song.getThumbnail()
//            )
//        }, supportingContent = {
//            Text(
//                text = song.getSongArtist() + " \u2022 " + song.getDuration(),
//                style = MaterialTheme.typography.titleSmall,
//                color = MaterialTheme.colorScheme.tertiary
//            )
//        }, modifier = Modifier
//            .padding(horizontal = 12.dp)
//            .fillMaxWidth()
//            .height(60.dp)
//    )
//
//}
//
@Composable
fun SongItemContent(
    modifier: Modifier = Modifier, song: Song,
    onSongClick: () -> Unit, onMoreChoice: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .clip(RoundedCornerShape(DefaultCornerSize))
            .size(56.dp)
            .aspectRatio(1f)

        Thumbnail(
            modifier = imageModifier,
            thumbnailSource = song.getThumbnail(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.getSongTitle(),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${song.getSongArtist()} \u00B7 ${song.getDuration()}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        IconButton(onClick = onMoreChoice) {
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
