package nd.phuc.core.domain.repository.abstraction

import kotlinx.coroutines.flow.Flow
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.model.Playlist
import nd.phuc.core.domain.model.Song

interface SongRepository<S : Song> {
    val allSongs: Flow<List<S>>
    val playlist: Flow<List<Playlist<S>>>

    suspend fun toggleLike(value: S)
    suspend fun getSongs()

    suspend fun createPlaylist(name: String, songs: List<S> = emptyList())

    suspend fun addSongToPlaylist(playlistId: Long, song: S)
}

interface LocalSongRepository : SongRepository<LocalSong>