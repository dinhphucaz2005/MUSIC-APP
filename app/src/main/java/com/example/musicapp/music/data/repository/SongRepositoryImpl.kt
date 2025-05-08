package com.example.musicapp.music.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import com.example.musicapp.music.data.LocalDataSource
import com.example.musicapp.music.data.RoomDataSource
import com.example.musicapp.music.data.database.entity.PlaylistEntity
import com.example.musicapp.music.data.database.entity.SongEntity
import com.example.musicapp.music.domain.model.FirebaseSong
import com.example.musicapp.music.domain.model.LocalSong
import com.example.musicapp.music.domain.model.Playlist
import com.example.musicapp.music.domain.model.Song
import com.example.musicapp.music.domain.model.ThumbnailSource
import com.example.musicapp.music.domain.model.YoutubeSong
import com.example.musicapp.music.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val context: Context,
    private val roomDataSource: RoomDataSource,
    private val localDataSource: LocalDataSource
) : SongRepository {

    override suspend fun getSongsFromPlaylist(playlistId: Int): List<Song> {
        val retriever = MediaMetadataRetriever()
        val songs = roomDataSource.getSongsByPlaylistId(playlistId).mapNotNull {
            when (it.type) {
                SongEntity.LOCAL_SONG -> localDataSource.getSongByPath(path = it.audioSource)
                    ?.copy(id = it.id.toString())

                SongEntity.FIREBASE_SONG -> FirebaseSong(
                    id = it.id.toString(),
                    title = it.title ?: "Unknown",
                    artist = it.artists?.get(0)?.name ?: "Unknown",
                    audioUrl = it.audioSource,
                    thumbnailSource = ThumbnailSource.FromUrl(it.thumbnail),
                    durationMillis = it.durationMillis,
                )

                SongEntity.YOUTUBE_SONG -> YoutubeSong(
                    id = it.id.toString(),
                    mediaId = it.audioSource,
                    title = it.title ?: "Unknown",
                    artists = it.artists ?: emptyList(),
                    thumbnail = it.thumbnail ?: "",
                    durationMillis = it.durationMillis,
                )

                else -> null
            }
        }
        retriever.release()
        return songs
    }

    // use for getLikedSongs  to make sure that local songs are loaded before getting liked songs
    private val _isReady = MutableStateFlow(false)
    private val isReady: StateFlow<Boolean> get() = _isReady

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLikedSongs(): Flow<List<Song>> {
        return isReady.filter { it }
            .flatMapLatest {
                roomDataSource.getLikedSongs()
                    .map { songEntities ->
                        songEntities.mapNotNull { entity ->
                            when (entity.type) {
                                SongEntity.LOCAL_SONG -> {
                                    val song = localDataSource.getSongByPath(entity.audioSource)
                                    song?.copy(id = entity.id.toString())
                                }

                                SongEntity.FIREBASE_SONG -> FirebaseSong(
                                    id = entity.id.toString(),
                                    title = entity.title ?: "Unknown",
                                    artist = entity.artists?.getOrNull(0)?.name ?: "Unknown",
                                    audioUrl = entity.audioSource,
                                    thumbnailSource = ThumbnailSource.FromUrl(entity.thumbnail),
                                    durationMillis = entity.durationMillis,
                                )

                                SongEntity.YOUTUBE_SONG -> YoutubeSong(
                                    id = entity.id.toString(),
                                    mediaId = entity.audioSource,
                                    title = entity.title ?: "Unknown",
                                    artists = entity.artists ?: emptyList(),
                                    thumbnail = entity.thumbnail ?: "",
                                    durationMillis = entity.durationMillis,
                                )

                                else -> null
                            }
                        }
                    }
            }
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
        return withContext(Dispatchers.IO) {
            val songs = localDataSource.get(context)
            _isReady.value = true
            songs
        }
    }

    override fun getPlayLists(): Flow<List<Playlist>> =
        roomDataSource.getPlayLists().map {
            it.mapNotNull { entity ->
                if (entity.id == null)
                    null
                else
                    Playlist(id = entity.id, name = entity.name)
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