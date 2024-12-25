package com.example.musicapp.other.data

import com.example.musicapp.other.data.database.AppDAO
import com.example.musicapp.other.data.database.LikedSongDAO
import com.example.musicapp.other.data.database.entity.LikedSong
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.data.database.entity.SongEntity
import com.example.musicapp.other.data.database.entity.toEntity
import com.example.musicapp.other.domain.model.LocalSong
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSource @Inject constructor(
    private val dao: AppDAO,
    private val likedSongDAO: LikedSongDAO
) {

    suspend fun createNewPlayList(name: String) {
        val id = UUID.randomUUID().toString()
        dao.addPlayList(PlaylistEntity(id, name))
    }


    suspend fun savePlayList(id: String, name: String) {
        dao.updatePlayList(PlaylistEntity(id, name))
    }

    suspend fun addSongsNew(localSongs: List<LocalSong>, playListId: String) {
        localSongs.forEach { song ->
            song.toEntity(playListId)?.let { dao.addSong(it) }
        }
    }

    suspend fun deletePlayList(id: String) = dao.deletePlayList(id)

    suspend fun deleteSongs(selectedSongIds: List<String>) {
        selectedSongIds.forEach { dao.deleteSong(it) }
    }

    suspend fun getPlayList(playListId: String): List<SongEntity> {
        return dao.getSongsByPlayListId(playListId)
    }

    suspend fun getPlayLists(): List<PlaylistEntity> = dao.getPlayLists()

    suspend fun getFavouriteSongs() = likedSongDAO.getLikedSongs()


    suspend fun deleteLikedSong(likedSong: LikedSong) =
        likedSongDAO.deleteLikedSong(likedSong)

    suspend fun getLikedSongById(id: String) = likedSongDAO.getLikedSongById(id)

    suspend fun addLikedSong(likedSong: LikedSong) =
        likedSongDAO.addLikedSong(likedSong)

    suspend fun deleteLikedSongById(id: Long) {
        likedSongDAO.deleteLikedSongById(id)
    }

}
