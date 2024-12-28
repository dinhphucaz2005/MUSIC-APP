package com.example.musicapp.other.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import com.example.musicapp.other.data.LocalDataSource
import com.example.musicapp.other.data.RoomDataSource
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.data.database.entity.SongEntity
import com.example.musicapp.other.domain.model.FirebaseSong
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.Playlist
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.other.domain.model.YoutubeSong
import com.example.musicapp.other.domain.repository.SongRepository
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val context: Context,
    private val roomDataSource: RoomDataSource,
    private val localDataSource: LocalDataSource
) : SongRepository {

    override suspend fun getSongsFromPlaylist(playlistId: Int): List<Song> {
        val retriever = MediaMetadataRetriever()
        val isLikedPlaylist = (playlistId == PlaylistEntity.LIKED_PLAYLIST_ID)
        val songs = roomDataSource.getSongsByPlaylistId(playlistId).mapNotNull {
            when (it.type) {
                SongEntity.LOCAL_SONG -> localDataSource.getSongByPath(path = it.audioSource)
                    ?.copy(isLiked = isLikedPlaylist, id = it.id.toString())

                SongEntity.FIREBASE_SONG -> FirebaseSong(
                    id = it.id.toString(),
                    title = it.title ?: "Unknown",
                    artist = it.artists?.get(0)?.name ?: "Unknown",
                    audioUrl = it.audioSource,
                    thumbnailSource = ThumbnailSource.FromUrl(it.thumbnail),
                    durationMillis = it.durationMillis,
                    isLiked = isLikedPlaylist
                )

                SongEntity.YOUTUBE_SONG -> YoutubeSong(
                    id = it.id.toString(),
                    mediaId = it.audioSource,
                    title = it.title ?: "Unknown",
                    artists = it.artists ?: emptyList(),
                    thumbnail = it.thumbnail ?: "",
                    durationMillis = it.durationMillis,
                    isLiked = isLikedPlaylist
                )

                else -> null
            }
        }
        retriever.release()
        return songs
    }

    override suspend fun createPlayList(name: String) {
        roomDataSource.createPlayList(name)
    }

    override suspend fun updatePlaylist(playlistId: Int, name: String) {
        roomDataSource.updatePlaylist(playlistId, name)
    }

    override suspend fun deletePlayList(id: Int) = roomDataSource.deletePlayList(id)

    override suspend fun addSongsToPlaylist(playListId: Int, localSongs: List<Song>) {
        roomDataSource.addSongs(localSongs, playListId)
    }

    override suspend fun deleteSongs(songIds: List<String>) {
        roomDataSource.deleteSongs(songIds)
    }

    override suspend fun getLocalSong(): List<LocalSong> {
        val likedSongs = roomDataSource.getSongsByPlaylistId(PlaylistEntity.LIKED_PLAYLIST_ID)
        val localSongs = localDataSource.get(context)
        for (song in localSongs) {
            if (likedSongs.any { it.audioSource == song.uri.path }) song.isLiked = true
        }
        return localSongs
    }

    override suspend fun getPlayLists(): List<Playlist> {
        return roomDataSource.getPlayLists().mapNotNull {
            if (it.id == null) null
            else Playlist(id = it.id, name = it.name)
        }
    }

    override suspend fun likeSong(song: Song) =
        roomDataSource.addSongs(listOf(song), PlaylistEntity.LIKED_PLAYLIST_ID)

    override suspend fun unlikeSong(song: Song) {
        val audioSource = when (song) {
            is LocalSong -> song.uri.path
            is FirebaseSong -> song.audioUrl
            is YoutubeSong -> song.mediaId
            else -> null
        } ?: return
        roomDataSource.deleteSongByAudioSource(audioSource, PlaylistEntity.LIKED_PLAYLIST_ID)
    }

}