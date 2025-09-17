package nd.phuc.musicapp.music.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import nd.phuc.core.model.LocalSong
import nd.phuc.musicapp.music.data.LocalDataSource
import nd.phuc.musicapp.music.data.RoomDataSource
import nd.phuc.musicapp.music.domain.repository.LocalSongRepository
import javax.inject.Inject

class LocalSongRepositoryImpl @Inject constructor(
    roomDataSource: RoomDataSource,
    private val localDataSource: LocalDataSource,
) : LocalSongRepository {

    private val mutex = Mutex()
    private val _allSongs = MutableStateFlow<List<LocalSong>>(emptyList())
    override val allSongs: Flow<List<LocalSong>> = combine(
        _allSongs,
        roomDataSource.getLikedSongs()
    ) { songs, likedSongs ->
        val likedPaths = likedSongs.map { it.filePath }.toSet()
        songs.map { it.copy(isLiked = likedPaths.contains(it.filePath)) }
    }

    override suspend fun getSongs() {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val songs = localDataSource.get()
                _allSongs.value = songs
            }
        }
    }
}