package nd.phuc.musicapp.music.data.repository

import android.content.Context
import nd.phuc.musicapp.music.data.LocalDataSource
import nd.phuc.musicapp.music.data.RoomDataSource
import nd.phuc.musicapp.music.data.database.entity.PlaylistEntity
import nd.phuc.musicapp.music.data.database.entity.SongEntity
import nd.phuc.core.model.FirebaseSong
import nd.phuc.core.model.LocalSong
import nd.phuc.core.model.Playlist
import nd.phuc.core.model.PlaylistId
import nd.phuc.core.model.Song
import nd.phuc.core.model.SongId
import nd.phuc.core.model.YoutubeSong
import nd.phuc.musicapp.music.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalSongRepository @Inject constructor(
    private val context: Context,
    private val roomDataSource: RoomDataSource,
    private val localDataSource: LocalDataSource
) : SongRepository {

    private val mutex = Mutex()
    private val _allSongs = MutableStateFlow<List<Song>>(emptyList())
    override val allSongs: Flow<List<Song>> = _allSongs.asStateFlow()

    private val _likedSongs = MutableStateFlow<Set<SongId>>(emptySet())
    override val likedSongs: Flow<Set<SongId>> = _likedSongs.asStateFlow()

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    override val playlists: Flow<List<Playlist>> = _playlists.asStateFlow()
    override suspend fun getSongs() {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val songs = localDataSource.get(context)
                _allSongs.value = songs
            }
        }
    }

    override suspend fun addSong(song: Song) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleLike(songId: SongId) {
        TODO("Not yet implemented")
    }

    override suspend fun createPlaylist(name: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun addSongToPlaylist(
        playlistId: PlaylistId,
        song: Song
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeSongFromPlaylist(
        playlistId: PlaylistId,
        songId: SongId
    ) {
        TODO("Not yet implemented")
    }

    @Deprecated("No longer used")
    override suspend fun getSongsFromPlaylist(playlistId: Int): List<Song> {
        throw NotImplementedError("Not yet implemented")
//        val retriever = MediaMetadataRetriever()
//        val songs = roomDataSource.getSongsByPlaylistId(playlistId).mapNotNull {
//            when (it.type) {
//                SongEntity.LOCAL_SONG -> localDataSource.getSongByPath(path = it.audioSource)
//                    ?.copy(id = it.id.toString())
//
//                SongEntity.FIREBASE_SONG -> FirebaseSong(
//                    id = it.id.toString(),
//                    title = it.title ?: "Unknown",
//                    artist = it.artists?.get(0)?.name ?: "Unknown",
//                    audioUrl = it.audioSource,
//                    thumbnailSource = ThumbnailSource.FromUrl(it.thumbnail),
//                    durationMillis = it.durationMillis,
//                )
//
//                SongEntity.YOUTUBE_SONG -> YoutubeSong(
//                    id = it.id.toString(),
//                    mediaId = it.audioSource,
//                    title = it.title ?: "Unknown",
//                    artists = it.artists ?: emptyList(),
//                    thumbnail = it.thumbnail ?: "",
//                    durationMillis = it.durationMillis,
//                )
//
//                else -> null
//            }
//        }
//        retriever.release()
//        return songs
    }

    // use for getLikedSongs  to make sure that local songs are loaded before getting liked songs
    private val _isReady = MutableStateFlow(false)
    private val isReady: StateFlow<Boolean> get() = _isReady

    @OptIn(ExperimentalCoroutinesApi::class)
//    override fun getLikedSongs(): Flow<List<Song>> {
//        throw NotImplementedError("Not yet implemented")
//        return isReady.filter { it }
//            .flatMapLatest {
//                roomDataSource.getLikedSongs()
//                    .map { songEntities ->
//                        songEntities.mapNotNull { entity ->
//                            when (entity.type) {
//                                SongEntity.LOCAL_SONG -> {
//                                    val song = localDataSource.getSongByPath(entity.audioSource)
//                                    song?.copy(id = entity.id.toString())
//                                }
//
//                                SongEntity.FIREBASE_SONG -> FirebaseSong(
//                                    id = entity.id.toString(),
//                                    title = entity.title ?: "Unknown",
//                                    artist = entity.artists?.getOrNull(0)?.name ?: "Unknown",
//                                    audioUrl = entity.audioSource,
//                                    thumbnailSource = ThumbnailSource.FromUrl(entity.thumbnail),
//                                    durationMillis = entity.durationMillis,
//                                )
//
//                                SongEntity.YOUTUBE_SONG -> YoutubeSong(
//                                    id = entity.id.toString(),
//                                    mediaId = entity.audioSource,
//                                    title = entity.title ?: "Unknown",
//                                    artists = entity.artists ?: emptyList(),
//                                    thumbnail = entity.thumbnail ?: "",
//                                    durationMillis = entity.durationMillis,
//                                )
//
//                                else -> null
//                            }
//                        }
//                    }
//            }
//    }

    @Deprecated("No longer used")
    override suspend fun createPlayList(name: String) {
        roomDataSource.createPlayList(name)
    }

    @Deprecated("No longer used")
    override suspend fun updatePlaylist(playlistId: Int, name: String) {
        roomDataSource.updatePlaylist(playlistId, name)
    }

    @Deprecated("No longer used")
    override suspend fun deletePlayList(id: Int) = roomDataSource.deletePlayList(id)

    @Deprecated("No longer used")
    override suspend fun addSongsToPlaylist(playListId: Int, localSongs: List<Song>) {
        roomDataSource.addSongs(localSongs, playListId)
    }

    @Deprecated("No longer used")
    override suspend fun deleteSongs(songIds: List<String>) {
        roomDataSource.deleteSongs(songIds)
    }

    @Deprecated("No longer used")
    override suspend fun getLocalSong(): List<LocalSong> {
        return withContext(Dispatchers.IO) {
            val songs = localDataSource.get(context)
            _isReady.value = true
            songs
        }
    }

    override fun getPlayLists(): Flow<List<Playlist>> =
        roomDataSource.getPlayLists().map {
            it.map { entity -> Playlist(id = entity.id, name = entity.name) }
        }

    @Deprecated("No longer used")
    override suspend fun likeSong(song: Song) =
        roomDataSource.addSongs(listOf(song), -1/*TODO*/)

    @Deprecated("No longer used")
    override suspend fun unlikeSong(song: Song) {
        val audioSource = when (song) {
            is LocalSong -> song.uri.path
            is FirebaseSong -> song.audioUrl
            is YoutubeSong -> song.mediaId
            else -> null
        } ?: return
        roomDataSource.deleteSongByAudioSource(audioSource, -1/*TODO*/)
    }

    @Deprecated("No longer used")
    override suspend fun savePlaylist(
        name: String, description: String, createdBy: String, songs: List<SongEntity>
    ) {
        roomDataSource.savePlaylist(name, description, createdBy, songs)
    }

    @Deprecated("No longer used")
    override suspend fun getPlaylists(): List<PlaylistEntity> {
        // get 1st time from flow
        val playlists = roomDataSource.getPlayLists()
            .map { it.map { entity -> entity } }
            .first()
        return playlists
    }

}