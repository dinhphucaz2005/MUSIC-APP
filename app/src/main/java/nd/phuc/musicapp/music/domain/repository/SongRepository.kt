package nd.phuc.musicapp.music.domain.repository

import nd.phuc.musicapp.music.data.database.entity.PlaylistEntity
import nd.phuc.musicapp.music.data.database.entity.SongEntity
import nd.phuc.musicapp.music.domain.model.LocalSong
import nd.phuc.musicapp.music.domain.model.Playlist
import nd.phuc.musicapp.music.domain.model.PlaylistId
import nd.phuc.musicapp.music.domain.model.Song
import nd.phuc.musicapp.music.domain.model.SongId
import kotlinx.coroutines.flow.Flow

interface SongRepository {

    @Deprecated("No longer used")
    suspend fun getSongsFromPlaylist(playlistId: Int): List<Song>

    @Deprecated("No longer used")
    suspend fun createPlayList(name: String)

    @Deprecated("No longer used")
    suspend fun updatePlaylist(playlistId: Int, name: String)

    @Deprecated("No longer used")
    suspend fun deletePlayList(id: Int)

    @Deprecated("No longer used")
    suspend fun addSongsToPlaylist(playListId: Int, localSongs: List<Song>)

    @Deprecated("No longer used")
    suspend fun deleteSongs(songIds: List<String>)

    @Deprecated("No longer used")
    suspend fun getLocalSong(): List<LocalSong>

    fun getPlayLists(): Flow<List<Playlist>>

    @Deprecated("No longer used")
    suspend fun likeSong(song: Song)

    @Deprecated("No longer used")
    suspend fun unlikeSong(song: Song)

    @Deprecated("No longer used")
    suspend fun savePlaylist(
        name: String,
        description: String,
        createdBy: String,
        songs: List<SongEntity>
    )

    @Deprecated("No longer used")
    suspend fun getPlaylists(): List<PlaylistEntity>

    val allSongs: Flow<List<Song>>
    val likedSongs: Flow<Set<SongId>>
    val playlists: Flow<List<Playlist>>

    suspend fun getSongs()
    suspend fun addSong(song: Song)
    suspend fun toggleLike(songId: SongId)
    suspend fun createPlaylist(name: String): String
    suspend fun addSongToPlaylist(playlistId: PlaylistId, song: Song)
    suspend fun removeSongFromPlaylist(playlistId: PlaylistId, songId: SongId)
}