package com.example.musicapp.other.presentation.ui.screen.playlist

//import android.annotation.SuppressLint
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.media3.common.util.UnstableApi
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.dragselectcompose.grid.indicator.internal.RadioButtonUnchecked
//import com.example.musicapp.constants.DefaultCornerSize
//import com.example.musicapp.core.presentation.components.Thumbnail
//import com.example.musicapp.ui.theme.MyMusicAppTheme
//import com.example.musicapp.di.FakeModule
//import com.example.musicapp.other.domain.model.LocalSong
//import com.example.musicapp.other.viewmodels.HomeViewModel
//import com.example.musicapp.other.presentation.ui.screen.playlist.PlaylistViewModel
//import kotlin.random.Random
//
//@UnstableApi
//@Preview
//@Composable
//fun PreviewScreen() {
//    MyMusicAppTheme {
//        PlayListEdit(
//            Random.nextInt(),
//            rememberNavController(),
//            FakeModule.providePlaylistViewModel(),
//            FakeModule.provideHomeViewModel()
//        )
//    }
//}
//
//@SuppressLint("UnsafeOptInUsageError")
//@Composable
//fun PlayListEdit(
//    playlistId: Int,
//    navController: NavController,
//    viewModel: PlaylistViewModel,
//    homeViewModel: HomeViewModel,
//) {
//
//}
//
//
//@UnstableApi
//@Composable
//fun SelectableSongItem(
//    modifier: Modifier = Modifier, localSong: LocalSong, isSelected: Boolean = false,
//) {
//    Row(
//        modifier = modifier, verticalAlignment = Alignment.CenterVertically
//    ) {
//        val imageModifier = Modifier
//            .clip(RoundedCornerShape(DefaultCornerSize))
//            .fillMaxHeight()
//            .aspectRatio(1f)
//
//        Thumbnail(thumbnailSource = localSong.thumbnailSource, modifier = imageModifier)
//
//        Text(
//            text = localSong.title,
//            modifier = Modifier
//                .weight(1f)
//                .padding(start = 8.dp),
//            style = MaterialTheme.typography.titleSmall,
//            color = MaterialTheme.colorScheme.primary,
//        )
//        Icon(
//            imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
//            tint = MaterialTheme.colorScheme.primary,
//            contentDescription = null,
//            modifier = Modifier.padding(6.dp)
//        )
//    }
//}
