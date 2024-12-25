package com.example.musicapp.other.data.repository

import android.content.Context
import com.example.musicapp.extension.getFileId
import com.example.musicapp.other.data.LocalDataSource
import com.example.musicapp.other.data.RoomDataSource
import com.example.musicapp.other.data.database.entity.LikedSong
import com.example.musicapp.other.domain.model.FirebaseSong
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.PlayList
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.other.domain.model.YoutubeSong
import com.example.musicapp.other.domain.repository.SongRepository
import java.io.File
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val context: Context,
    private val roomDataSource: RoomDataSource,
    private val localDataSource: LocalDataSource
) : SongRepository {


    override suspend fun getSongsFromPlaylist(playListId: String): List<LocalSong> {
        val localSong = localDataSource.get(context)
        return roomDataSource.getPlayList(playListId).mapNotNull {
            val file = File(it.path)
            if (file.exists()) {
                localSong.find { song -> song.id == file.getFileId() }?.copy(id = it.id)
            } else null
        }
    }

    override suspend fun createPlayList(name: String) {
        roomDataSource.createNewPlayList(name)
    }

    override suspend fun savePlayList(id: String, name: String) {
        roomDataSource.savePlayList(id, name)
    }

    override suspend fun deletePlayList(id: String) = roomDataSource.deletePlayList(id)


    override suspend fun addSongsToPlaylist(playListId: String, localSongs: List<LocalSong>) {
        roomDataSource.addSongsNew(localSongs, playListId)
    }

    override suspend fun deleteSongs(selectedSongIds: List<String>) =
        roomDataSource.deleteSongs(selectedSongIds)

    override suspend fun getLocalSong(): List<LocalSong> = localDataSource.get(context)

    override suspend fun getSongByPath(path: String): LocalSong? =
        localDataSource.getSongByPath(context, path)


    override suspend fun getPlayLists(): List<PlayList> = roomDataSource.getPlayLists().map {
        PlayList(id = it.id, name = it.name)
    }

    override suspend fun likeSong(song: Song) {
        roomDataSource.addLikedSong(song.toLikedSong())
    }

    override suspend fun unlikeSong(id: Long) {
        roomDataSource.deleteLikedSongById(id)
    }

    override suspend fun getLikedSongs(): List<Song> {
        return roomDataSource.getFavouriteSongs().mapNotNull {
            when (it.type) {

                LikedSong.LOCAL -> getSongByPath(it.audioSource)?.copy(id = it.id.toString())

                LikedSong.FIREBASE -> FirebaseSong(
                    id = it.id.toString(),
                    title = it.title ?: "Unknown",
                    artist = it.artist ?: "Unknown",
                    thumbnailSource = ThumbnailSource.FromUrl(it.thumbnail),
                    durationMillis = it.durationMillis,
                    audioUrl = it.audioSource
                )

//                LikedSong.YOUTUBE -> YoutubeSong(
//                    id = it.id.toString(),
//                    title = it.title ?: "Unknown",
//                    artists = emptyList(),
//                    thumbnail = it.thumbnail ?: "",
//                    duration = it.durationMillis ?: 0
//                )

                else -> null
            }
        }
    }
}