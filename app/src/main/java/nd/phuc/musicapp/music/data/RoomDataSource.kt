package nd.phuc.musicapp.music.data

import nd.phuc.musicapp.music.data.database.AppDAO
import nd.phuc.musicapp.music.data.database.entity.PlaylistEntity
import nd.phuc.musicapp.music.data.database.entity.SongEntity
import nd.phuc.core.model.LocalSong
import nd.phuc.core.model.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSource @Inject constructor(
    private val dao: AppDAO,
) {

    suspend fun savePlaylist(
        name: String,
        description: String,
        createdBy: String,
        songs: List<SongEntity>
    ) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            createdBy = createdBy,
            createdAt = System.currentTimeMillis(),
            songs = songs
        )
        dao.savePlaylist(playlist)
    }


    suspend fun createPlayList(name: String) = dao.addPlayList(
        PlaylistEntity(
            name = name,
            description = "",
            createdBy = "",
            createdAt = System.currentTimeMillis(),
            songs = emptyList()
        )
    )

    suspend fun updatePlaylist(id: Int, name: String) = dao.updatePlayList(PlaylistEntity(id, name))
    suspend fun addSongs(songs: List<Song>, playListId: Int) {
//        songs.forEach { song ->
//            val audioSource = when (song) {
//                is LocalSong -> song.uri.path
//                is YoutubeSong -> song.id
//                else -> null
//            }
//            val exists: Boolean = audioSource
//                ?.let { dao.isSongExists(it.toString(), playListId) }
//                ?.let { it > 0 } == true
//
//            if (exists) return
//
//            SongEntity.create(song = song, playlistId = playListId)?.let { dao.addSong(it) }
//        }
    }

    suspend fun getSongsByPlaylistId(playListId: Int): List<SongEntity> =
        dao.getSongsByPlaylistId(playListId)

    suspend fun deletePlayList(id: Int) = dao.deletePlayList(id)

    suspend fun deleteSongs(songIds: List<String>) {
        songIds.forEach {
            val id = it.toIntOrNull() ?: return
            dao.deleteSongById(id)
        }
    }

    fun getPlayLists(): Flow<List<PlaylistEntity>> = dao.getPlayLists()

    suspend fun deleteSongByAudioSource(audioSource: String, playListId: Int) =
        dao.deleteSongByAudioSource(audioSource, playListId)

    suspend fun deleteSongById(id: Int) = dao.deleteSongById(id)

    fun getLikedSongs(): Flow<List<SongEntity>> =
        dao.getSongsFromPlaylist(-1/*TODO*/)

}
