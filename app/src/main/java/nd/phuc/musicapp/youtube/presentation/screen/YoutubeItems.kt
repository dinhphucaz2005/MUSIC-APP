package nd.phuc.musicapp.youtube.presentation.screen

//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.innertube.models.SongItem
//import nd.phuc.musicapp.constants.DefaultCornerSize
//import nd.phuc.musicapp.constants.SongItemHeight
//import nd.phuc.musicapp.core.presentation.components.MyListItem
//import nd.phuc.musicapp.core.presentation.components.Thumbnail
//import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
//import nd.phuc.musicapp.ui.theme.white
//import nd.phuc.musicapp.extension.toArtistString
//import nd.phuc.musicapp.extension.toDurationString
//import nd.phuc.musicapp.other.domain.model.ThumbnailSource
//
//@Composable
//fun SongItemFromYoutube(modifier: Modifier = Modifier, song: SongItem, onClick: () -> Unit) {
//    MyListItem(
//        headlineContent = {
//            Text(
//                text = song.title,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                color = white,
//                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
//            )
//        },
//        leadingContent = {
//            val imageModifier = Modifier
//                .clip(RoundedCornerShape(DefaultCornerSize))
//                .fillMaxHeight()
//                .aspectRatio(1f)
//            Thumbnail(
//                modifier = imageModifier,
//                thumbnailSource = ThumbnailSource.FromUrl(song.thumbnail)
//            )
//        },
//        supportingContent = {
//            Text(
//                text = "${song.artists.toArtistString()} \u00B7 ${song.duration.toDurationString()}",
//                color = white,
//            )
//        },
//        trailingContent = {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Default.MoreVert, contentDescription = null,
//                    tint = white
//                )
//            }
//        },
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .height(SongItemHeight)
//    )
//}
//
//@Preview
//@Composable
//private fun SongsPreview() {
//    MyMusicAppTheme {
//        Column {
//            Songs(songs = List(20) {
//                SongItem.unidentifiedSong().copy(id = it.toString())
//            }) {
//                 do nothing
//            }
//        }
//    }
//}
//
//@Composable
//fun Songs(
//    modifier: Modifier = Modifier,
//    songs: List<SongItem>,
//    onClick: (SongItem) -> Unit
//) {
//    val maxRow = 4
//    val state = rememberPagerState { songs.size / maxRow }
//    HorizontalPager(state = state) { index ->
//        Column(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(end = 12.dp)
//                .wrapContentHeight(),
//            verticalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//            for (i in index * maxRow until (index + 1) * maxRow) {
//                if (i < songs.size) {
//                    val song = songs[i]
//                    SongItemFromYoutube(
//                        song = song, modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(end = 12.dp)
//                    ) {
//                        onClick(song)
//                    }
//                }
//            }
//        }
//    }
//}
