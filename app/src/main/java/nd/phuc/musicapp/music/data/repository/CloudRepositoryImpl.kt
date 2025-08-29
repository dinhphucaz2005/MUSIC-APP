package nd.phuc.musicapp.music.data.repository

//import nd.phuc.musicapp.music.data.FirebaseDataSource
//import nd.phuc.musicapp.music.domain.model.FirebaseSong
//import nd.phuc.musicapp.music.domain.model.LocalSong
//import nd.phuc.musicapp.music.domain.model.ThumbnailSource
//import nd.phuc.musicapp.music.domain.repository.CloudRepository
//import javax.inject.Inject
//
//class CloudRepositoryImpl @Inject constructor(
//    private val firebaseDataSource: FirebaseDataSource
//) : CloudRepository {
//
//
//    override suspend fun load(): List<FirebaseSong> {
//        val serverSongs = firebaseDataSource.load()
//        return serverSongs.map {
//            FirebaseSong(
//                id = it.id,
//                title = it.title,
//                artist = it.artist,
//                audioUrl = it.songUri,
//                thumbnailSource = ThumbnailSource.FromUrl(it.thumbnailUri),
//                durationMillis = it.durationMillis
//            )
//        }
//    }
//
//    override fun upload(localSongs: List<LocalSong>) {
//        firebaseDataSource.uploadSongs(localSongs)
//    }
//}