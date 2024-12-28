package com.example.musicapp.other.data

import com.example.musicapp.other.data.database.AppDAO
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.data.database.entity.SongEntity
import com.example.musicapp.other.domain.model.FirebaseSong
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.YoutubeSong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSource @Inject constructor(
    private val dao: AppDAO,
) {

    suspend fun createPlayList(name: String) = dao.addPlayList(PlaylistEntity(name = name))

    suspend fun updatePlaylist(id: Int, name: String) = dao.updatePlayList(PlaylistEntity(id, name))

    suspend fun addSongs(songs: List<Song>, playListId: Int) {
        songs.forEach { song ->
            val audioSource = when (song) {
                is LocalSong -> song.uri.path
                is FirebaseSong -> song.audioUrl
                is YoutubeSong -> song.id
                else -> null
            }
            val exists: Boolean = audioSource
                ?.let { dao.isSongExists(it, playListId) }
                ?.let { it > 0 } == true

            if (exists) return

            SongEntity.create(song = song, playlistId = playListId)?.let { dao.addSong(it) }
        }
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

    suspend fun getPlayLists(): List<PlaylistEntity> = dao.getPlayLists()

    suspend fun deleteSongByAudioSource(audioSource: String, playListId: Int) =
        dao.deleteSongByAudioSource(audioSource, playListId)

    suspend fun deleteSongById(id: Int) = dao.deleteSongById(id)

}
