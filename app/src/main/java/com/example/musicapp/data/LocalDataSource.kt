package com.example.musicapp.data

import android.content.Context
import android.provider.MediaStore
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.getId
import com.example.musicapp.helper.MediaRetrieverHelper
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor() {

    private val caching = mutableMapOf<Long, Int>() // <song id, index in local files>

    suspend fun fetch(
        context: Context, currentSongs: List<Song>
    ): List<Song> {
        val invalidSongIds = getInvalidSongIds(currentSongs)

        val newSong = currentSongs
            .filter { it.id !in invalidSongIds }
            .toMutableList()

        val uninitializedFilePaths = getUninitializedFilePaths(context)
        newSong.addAll(MediaRetrieverHelper.getSongsInfo(uninitializedFilePaths))
        updateCache(newSong)
        return newSong
    }

    private fun getInvalidSongIds(currentSongs: List<Song>): List<Long> {
        return currentSongs.mapNotNull { song ->
            val file = song.getPath()?.let { File(it) }
            if (file?.exists() == true && caching.contains(file.getId())) {
                null // // Song is valid and in caching, return null
            } else {
                song.id // Song is invalid, return its id
            }
        }
    }

    private fun getUninitializedFilePaths(context: Context): List<String> {
        val paths = getAllLocalFilePaths(context)
        return paths.filter { path ->
            val id = File(path).getId()
            !caching.contains(id) // Add song to caching if it is not in caching
        }
    }


    private fun getAllLocalFilePaths(context: Context): List<String> {
        val filePaths = mutableListOf<String>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                val filePath = it.getString(dataIndex)
                filePaths.add(filePath)
            }
        }
        return filePaths
    }

    private fun updateCache(newSongs: List<Song>) {
        caching.clear()
        newSongs.forEachIndexed { index, song ->
            caching[song.id] = index
        }
    }

    fun getSongInfo(localSongId: Long): Int? { // Index in local files
        return caching[localSongId]
    }
}
