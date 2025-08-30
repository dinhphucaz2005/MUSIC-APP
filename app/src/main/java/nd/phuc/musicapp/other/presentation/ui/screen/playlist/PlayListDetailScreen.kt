package nd.phuc.musicapp.other.presentation.ui.screen.playlist

//import android.annotation.SuppressLint
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import nd.phuc.core.model.DefaultCornerSize
//import nd.phuc.core.presentation.components.LazyColumnWithAnimation2
//import nd.phuc.core.presentation.components.MyListItem
//import nd.phuc.core.presentation.components.Thumbnail
//import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
//import nd.phuc.musicapp.ui.theme.white
//import nd.phuc.musicapp.di.FakeModule
//import nd.phuc.musicapp.other.domain.model.Playlist
//import nd.phuc.musicapp.other.domain.model.ThumbnailSource
//import nd.phuc.musicapp.song.SongItemContent
//
//@SuppressLint("UnsafeOptInUsageError")
//@Preview
//@Composable
//fun PlaylistDetailPreview() {
//    MyMusicAppTheme {
//        PlaylistDetailContent(
//            playlist = FakeModule.playlist
//        )
//    }
//}
//
//
//@Composable
//private fun PlaylistDetailContent(
//    playlist: Playlist,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(12.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        MyListItem(
//            leadingContent = {
//                Thumbnail(
//                    thumbnailSource = ThumbnailSource.FromBitmap(null),
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .aspectRatio(1f)
//                        .clip(RoundedCornerShape(DefaultCornerSize))
//                )
//            },
//            headlineContent = {
//                Text(
//                    text = playlist.name,
//                    style = MaterialTheme.typography.titleMedium,
//                    color = white
//                )
//            },
//            supportingContent = {
//                Text(
//                    text = "${playlist.songs.size} songs",
//                    style = MaterialTheme.typography.titleSmall,
//                    color = white
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(140.dp)
//        )
//
//        LazyColumnWithAnimation2(
//            items = playlist.songs,
//            key = { _, item -> item.id },
//            modifier = Modifier
//                .fillMaxSize()
//        ) { itemModifier, _, song ->
//            SongItemContent(
//                song = song,
//                modifier = itemModifier,
//                onSongClick = {},
//                onMoreChoice = {}
//            )
//        }
//
//    }
//}