package nd.phuc.musicapp.music.domain.repository

import kotlinx.coroutines.flow.Flow
import nd.phuc.core.model.LocalSong
import nd.phuc.core.model.Song

interface SongRepository<S : Song> {
    val allSongs: Flow<List<S>>
    suspend fun getSongs()
}

interface LocalSongRepository : SongRepository<LocalSong>