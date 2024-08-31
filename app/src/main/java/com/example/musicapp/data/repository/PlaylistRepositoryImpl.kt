package com.example.musicapp.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import com.example.musicapp.callback.AppResource
import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.data.database.entity.PlaylistEntity
import com.example.musicapp.data.database.entity.SongEntity
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.helper.MediaRetrieverHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: AppDAO
) : PlaylistRepository {

    companion object {
        const val TAG = "PlaylistRepositoryImpl"
        const val RESTRICTED_PLAYLIST_ID = -1L
        const val RESTRICTED_PLAYLIST_NAME = "Local Music"
    }

    private val fileModificationDates = ConcurrentHashMap<String, Long>()

    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)

    private val _localPlaylist = MutableStateFlow<Playlist?>(null)
    private val _allPlaylistFromDatabase = MutableStateFlow<List<Playlist>>(emptyList())

    private suspend fun fetchAllPlaylistFromDatabase(): List<Playlist> {
        return withContext(Dispatchers.IO) {
            val result = mutableListOf<Playlist>()
            val playlistDTO = dao.getPlaylist() // order by playlist.id, song.id
            if (playlistDTO.isEmpty())
                return@withContext result
            Log.d(TAG, "fetchAllPlaylistFromDatabase: $playlistDTO")
            result.add(Playlist(playlistDTO[0].id, playlistDTO[0].name))
            playlistDTO.forEach { dto ->
                if (dto.songId == null) {
                    return@forEach
                }
                if (result.last().id != dto.id) {
                    result.add(Playlist(dto.id, dto.name))
                }
                result.add(Playlist(dto.id, dto.name))
                if (dto.songPath == null) {
                    dao.deleteSongById(dto.songId)
                    return@forEach
                }
                val song = _localPlaylist.value?.songs?.find { song -> song.path == dto.songPath }
                if (song != null)
                    result.last().songs.add(song)
                else
                    dao.deleteSongById(dto.songId)
            }
            result
        }
    }

    private suspend fun fetchPlaylistFromLocal(): Playlist? {
        return withContext(Dispatchers.IO) {
            val filePaths = mutableListOf<String>()
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Audio.Media.DATA)
            val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
            val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
            val cursor =
                context.contentResolver.query(uri, projection, selection, null, sortOrder)
            cursor?.use {
                val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                while (it.moveToNext()) {
                    val filePath = it.getString(dataIndex)
                    filePaths.add(filePath)
                }
            }

            val currentSongs = _localPlaylist.value?.songs
            val songs = mutableListOf<Song>()
            var changed = false

            filePaths.forEachIndexed { index, path ->
                val file = File(path)
                val lastModified = file.lastModified()

                val song = if (fileModificationDates[path] == lastModified)
                    currentSongs?.find { it.path == path } else null

                if (song != null) {
                    songs.add(song)
                } else {
                    changed = true
                    MediaRetrieverHelper.getSongInfo(
                        MediaMetadataRetriever(),
                        path,
                        index.toLong()
                    ).let { songs.add(it) }
                }
                fileModificationDates[path] = lastModified
            }
            if (changed || songs.size != currentSongs?.size)
                Playlist(RESTRICTED_PLAYLIST_ID, RESTRICTED_PLAYLIST_NAME, songs)
            else
                null
        }
    }

    override suspend fun reload(): AppResource<Nothing> {
        return withContext(Dispatchers.IO) {
            try {
                val playlistFromLocal = async { fetchPlaylistFromLocal() }.await()
                val allPlaylistsFromDatabase = async { fetchAllPlaylistFromDatabase() }.await()
                playlistFromLocal?.let {
                    _localPlaylist.value = it
                    _currentPlaylist.value = it
                }
                _allPlaylistFromDatabase.value = allPlaylistsFromDatabase
                AppResource.Success(null)
            } catch (e: Exception) {
                AppResource.Error(e)
            }
        }
    }

    override suspend fun addPlaylist(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val playlistId = dao.addPlayList(PlaylistEntity(name = name))
            val playlist = Playlist(playlistId, name)
            _allPlaylistFromDatabase.value =
                _allPlaylistFromDatabase.value.toMutableList().apply { add(playlist) }
        }
    }

    override suspend fun savePlaylist(id: Long, name: String, songs: List<Song>) {
        CoroutineScope(Dispatchers.IO).launch {
            val playlist = _allPlaylistFromDatabase.value.find { it.id == id }
            if (playlist != null) {
                dao.addPlayList(PlaylistEntity(id, name))
                playlist.name = name
                songs.forEach {
                    if (it.path == null)
                        return@forEach
                    dao.addSong(
                        SongEntity(
                            playlistId = id,
                            path = it.path,
                            title = it.fileName
                        )
                    )
                }
                playlist.songs.addAll(songs)
            }
        }
    }

    override fun setLocal(index: Int) {
        if (_currentPlaylist.value?.id != RESTRICTED_PLAYLIST_ID) {
            _localPlaylist.value?.currentSong = index
            _currentPlaylist.value = _localPlaylist.value
        }
    }

    override fun setPlaylist(playlistId: Long, index: Int) {
        if (_currentPlaylist.value?.id != playlistId) {
            val target = _allPlaylistFromDatabase.value.find { it.id == playlistId }
            target?.currentSong = index
            _currentPlaylist.value = target
        }
    }

    override fun observeLocalPlaylist(): StateFlow<Playlist?> = _localPlaylist.asStateFlow()

    override fun observeCurrentPlaylist(): StateFlow<Playlist?> = _currentPlaylist.asStateFlow()

    override fun observeAllPlaylistsFromDatabase(): StateFlow<List<Playlist>> =
        _allPlaylistFromDatabase.asStateFlow()

    override suspend fun deleteSongs(deleteSongIndex: MutableList<Int>, id: Long) {
        return withContext(Dispatchers.IO) {
            val playlists = _allPlaylistFromDatabase.value.toMutableList()
            val playlistIndex = playlists.indexOfFirst { it.id == id }
            if (playlistIndex != -1) {
                val playlist =
                    playlists[playlistIndex].copy(songs = playlists[playlistIndex].songs.toMutableList())
                deleteSongIndex.sortDescending()
                deleteSongIndex.forEach {
                    playlist.songs[it].id?.let { it1 -> dao.deleteSongById(it1) }
                    playlist.songs.removeAt(it)
                }
                playlists[playlistIndex] = playlist
                _allPlaylistFromDatabase.value = playlists
            }
        }
    }

    override suspend fun deletePlaylist(id: Long) {
        return withContext(Dispatchers.IO) {
            val playlists = _allPlaylistFromDatabase.value.toMutableList()
            val playlistIndex = playlists.indexOfFirst { it.id == id }
            if (playlistIndex != -1) {
                playlists.removeAt(playlistIndex)
                _allPlaylistFromDatabase.value = playlists
            }
            dao.deletePlayList(id)
            dao.deleteSongByPlaylistId(id)
        }
    }
}