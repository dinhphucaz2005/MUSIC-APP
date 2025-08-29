package nd.phuc.musicapp.other.presentation.ui.screen.cloud

//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import nd.phuc.musicapp.LocalMediaControllerManager
//import nd.phuc.musicapp.core.presentation.components.LazyColumnWithAnimation2
//import nd.phuc.musicapp.core.presentation.components.LoadingScreen
//import nd.phuc.musicapp.di.fakeViewModel
//import nd.phuc.musicapp.other.domain.model.Queue
//import nd.phuc.musicapp.other.viewmodels.CloudViewModel
//import nd.phuc.musicapp.song.SongItemContent
//import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
//
//@Composable
//fun CloudScreen(viewModel: CloudViewModel = fakeViewModel<CloudViewModel>()) {
//
//    val mediaControllerManager = LocalMediaControllerManager.current ?: return
//
//    val songs by viewModel.songs.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    if (isLoading) {
//        LoadingScreen()
//    } else {
//        LazyColumnWithAnimation2(
//            modifier = Modifier.fillMaxSize(),
//            items = songs,
//            key = { _, item -> item.id },
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) { itemModifier, index, item ->
//            SongItemContent(
//                modifier = itemModifier.clickable(onClick = {
//                    mediaControllerManager.playQueue(
//                        songs = songs,
//                        index = index,
//                        id = Queue.FIREBASE_ID
//                    )
//                }),
//                song = item,
//                onSongClick = {},
//                onMoreChoice = {}
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun CloudScreenPreview() {
//    MyMusicAppTheme {
//        CloudScreen()
//    }
//}