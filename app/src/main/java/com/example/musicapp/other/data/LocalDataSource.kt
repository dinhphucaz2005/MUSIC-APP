package com.example.musicapp.other.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.example.musicapp.helper.MediaRetrieverHelper
import com.example.musicapp.other.domain.model.LocalSong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor() {

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


    suspend fun get(context: Context): List<LocalSong> {
        val paths = getAllLocalFilePaths(context)
        return MediaRetrieverHelper.extracts(paths)
    }

    suspend fun getSongByPath(context: Context, path: String): LocalSong? =
        withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {
                MediaRetrieverHelper.extract(retriever, path)
            } catch (e: Exception) {
                null
            } finally {
                retriever.release()
            }
        }

}
