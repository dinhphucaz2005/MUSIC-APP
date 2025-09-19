package nd.phuc.core.domain.repository.implementation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import nd.phuc.core.database.PlaylistDao
import nd.phuc.core.database.SongDao
import nd.phuc.core.database.entity.PlaylistEntity
import nd.phuc.core.database.entity.SongEntity
import nd.phuc.core.domain.LocalDataSource
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.Playlist
import nd.phuc.core.domain.model.ThumbnailSource
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import javax.inject.Inject

internal class LocalSongRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao,
) : LocalSongRepository {

    private val mutex = Mutex()
    private val _allSongs = MutableStateFlow<List<LocalSong>>(emptyList())
    override val allSongs: Flow<List<LocalSong>> = combine(
        _allSongs,
        songDao.getLikedSongs()
    ) { songs, likedSongs ->
        val likedPaths = likedSongs.map { it.filePath }.toSet()
        songs.map { it.copy(isLiked = likedPaths.contains(it.filePath)) }
    }

    override val playlist: Flow<List<Playlist<LocalSong>>> = combine(
        playlistDao.getPlaylists(),
        songDao.getSongs(),
        _allSongs
    ) { playlists, songs, localSong ->
        val songMap = songs.associateBy { it.filePath }
        playlists.map { playlist ->
            val playlistSongs = localSong.filter {
                songMap.containsKey(it.filePath) && songMap[it.filePath]?.playlistId == playlist.id
            }
            Playlist(
                id = playlist.id,
                name = playlist.name,
                songs = playlistSongs,
                thumbnailSource = if (playlist.coverUrl != null) {
                    ThumbnailSource.FromUrl(playlist.coverUrl)
                } else if (playlist.imagePath != null)
                    ThumbnailSource.FilePath(path = playlist.imagePath)
                else if (playlistSongs.isNotEmpty())
                    playlistSongs.random().thumbnailSource
                else
                    ThumbnailSource.None,
            )
        }
    }


    override suspend fun toggleLike(value: LocalSong) = songDao.toggleLike(value.filePath)

    override suspend fun getSongs() {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val songs = localDataSource.get()
                _allSongs.value = songs
            }
        }
    }

    override suspend fun createPlaylist(
        name: String,
        songs: List<LocalSong>,
    ) {
        val playlistId = playlistDao.addPlaylist(PlaylistEntity(name = name))
        songs.forEach {
            songDao.addSong(
                SongEntity(
                    filePath = it.filePath,
                    isFavourite = it.isLiked,
                    playlistId = playlistId,
                )
            )
        }
    }

    override suspend fun addSongToPlaylist(playlistId: Long, song: LocalSong) {
        songDao.addSong(
            SongEntity(
                filePath = song.filePath,
                isFavourite = song.isLiked,
                playlistId = playlistId,
            )
        )
    }
}