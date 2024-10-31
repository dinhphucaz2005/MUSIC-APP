package com.example.musicapp.data.repository

import android.content.Context
import android.provider.MediaStore
import com.example.musicapp.callback.AppResource
import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.data.database.entity.PlaylistEntity
import com.example.musicapp.data.database.entity.SongEntity
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.helper.LocalPlaylistCaching
import com.example.musicapp.helper.MediaRetrieverHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: AppDAO
) : PlaylistRepository {

    companion object {
        const val PLAYLIST_ID = -1L
        const val PLAYLIST_NAME = "Local Music"
    }

    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)

    private val _localPlaylist = MutableStateFlow<Playlist?>(null)
    private val _allPlaylistFromDatabase = MutableStateFlow<List<Playlist>>(emptyList())

    override suspend fun reload(): AppResource<Void> {
        try {
            val playlistFromLocal = fetchPlaylistFromLocal()
            playlistFromLocal?.songs?.forEach {
                println(it)
            }
            playlistFromLocal?.let {
                _localPlaylist.value = it
                _currentPlaylist.value = it
            }
            val allPlaylistsFromDatabase = fetchAllPlaylistFromDatabase()
            _allPlaylistFromDatabase.value = allPlaylistsFromDatabase
            return AppResource.Success(null)
        } catch (e: Exception) {
            return AppResource.Error(e)
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

    override fun setLocal(index: Int) {
        if (_currentPlaylist.value?.id != PLAYLIST_ID) {
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

    override fun observeCurrentPlaylist(): StateFlow<Playlist?> = _currentPlaylist.asStateFlow()

    override fun observeLocalPlaylist(): StateFlow<Playlist?> = _localPlaylist.asStateFlow()

    override fun observeAllPlaylistsFromDatabase(): StateFlow<List<Playlist>> =
        _allPlaylistFromDatabase.asStateFlow()

    override suspend fun deleteSongs(deleteSongIndex: MutableList<Int>, id: Long) {
        return withContext(Dispatchers.IO) {
            val playlists = _allPlaylistFromDatabase.value.toMutableList()
            val playlistIndex = playlists.indexOfFirst { it.id == id }
            if (playlistIndex != -1) {
                val playlist = playlists[playlistIndex].copy()
                deleteSongIndex.sortDescending()
                deleteSongIndex.forEach {
                    playlist.songs[it].id?.let { songId -> dao.deleteSongById(songId) }
                    playlist.songs.removeAt(it)
                }
                playlists[playlistIndex] = playlist
                _allPlaylistFromDatabase.value = playlists
            }
        }
    }

    private fun getAllLocalFilePaths(): List<String> {
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
        return filePaths
    }

    private suspend fun fetchAllPlaylistFromDatabase(): List<Playlist> {
        return withContext(Dispatchers.IO) {
            val result = mutableListOf<Playlist>()
            val playlistDTO =
                dao.getPlaylist() // order by playlist.id, song.id
            playlistDTO.firstOrNull()?.apply { result.add(Playlist(id, name)) }
            playlistDTO.forEach { dto ->
                if (result.last().id != dto.id)
                    result.add(Playlist(dto.id, dto.name))

                if (dto.songId == null) return@forEach

                if (dto.songPath == null) {
                    dao.deleteSongById(dto.songId)
                    return@forEach
                }
                println(_localPlaylist.value?.songs)
                _localPlaylist.value?.songs?.find { song -> song.path == dto.songPath }
                    ?.copy()
                    ?.let { result.last().songs.add(it) }
            }
            result
        }
    }

    private suspend fun fetchPlaylistFromLocal(): Playlist? {
        return withContext(Dispatchers.IO) {
            val currentSongs = _localPlaylist.value?.songs
            val songs = mutableListOf<Song>()

            val unInitializerFilePaths = mutableListOf<String>()
            val addPathToUnInitializer = { path: String -> unInitializerFilePaths.add(path) }

            getAllLocalFilePaths().let {
                if (currentSongs == null) {
                    unInitializerFilePaths.addAll(it)
                } else {
                    it.forEach { path ->
                        val index = LocalPlaylistCaching.check(path)
                        if (index == null) addPathToUnInitializer(path)
                        else songs.add(currentSongs[index])
                    }
                }
            }

            songs.addAll(MediaRetrieverHelper.getSongsInfo(unInitializerFilePaths))
            songs.sortWith(compareBy<Song> { it.author }.thenBy { it.title })
            for (i in songs.indices) songs[i].id = i.toLong()

            if (unInitializerFilePaths.isNotEmpty()) {
                songs.forEachIndexed { index, song -> LocalPlaylistCaching.add(song.path, index) }
                Playlist(PLAYLIST_ID, PLAYLIST_NAME, songs)
            } else null
        }
    }
}