package com.example.mymusicapp.data.repository

import android.media.MediaMetadataRetriever
import com.example.mymusicapp.data.database.AppDatabase
import com.example.mymusicapp.data.database.entity.PlaylistEntity
import com.example.mymusicapp.data.database.entity.SongEntity
import com.example.mymusicapp.domain.model.Playlist
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.PlaylistRepository
import com.example.mymusicapp.helper.MediaRetrieverHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(val database: AppDatabase) : PlaylistRepository {

    override suspend fun getPlaylist(): List<Playlist> {
        return withContext(Dispatchers.IO) {
            val songs: List<SongEntity> = database.appDAO().getSongs()
            val playLists: List<PlaylistEntity> = database.appDAO().getPlayLists()

            val playlistsMap = playLists.associateBy { it.id }
            val result = playLists.map { Playlist(it.id, it.name, mutableListOf()) }

            val retriever = MediaMetadataRetriever()
            try {
                songs.forEach { songEntity ->
                    MediaRetrieverHelper.getAllInfo(songEntity.path, retriever)?.let { song ->
                        playlistsMap[songEntity.id]?.let { playlistEntity ->
                            val playlist = result.find { it.id == playlistEntity.id }
                            playlist?.songs?.add(song)
                        }
                    }
                }
            } finally {
                retriever.release()
            }
            println(result)
            result
        }
    }


    override suspend fun addPlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

}
