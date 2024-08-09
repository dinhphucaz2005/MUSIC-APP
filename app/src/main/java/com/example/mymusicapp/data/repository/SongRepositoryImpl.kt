package com.example.mymusicapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.helper.MediaRetrieverHelper
import com.example.mymusicapp.helper.StringHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("UnsafeOptInUsageError")
class SongRepositoryImpl(private val context: Context) : SongFileRepository {

    companion object {
        const val TAG = "SongFileRepositoryImpl"
    }

    private val _songList = MutableStateFlow<List<Song>>(emptyList())
    private val songList: StateFlow<List<Song>> = _songList

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _songList.value = fetchAllAudioFiles()
        }
    }

    private suspend fun fetchAllAudioFiles(): List<Song> {
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
            MediaRetrieverHelper.getAllInfo(filePaths)
        }
    }

    override suspend fun getLocal(): StateFlow<List<Song>> {
        return songList
    }

    @OptIn(UnstableApi::class)
    override suspend fun search(searchQuery: String): List<Song> {
        return _songList.value.filter {
            StringHelper.matching(it.fileName, searchQuery) || StringHelper.matching(
                it.title ?: "",
                searchQuery
            )
        }
    }

    override fun reload() {
        CoroutineScope(Dispatchers.IO).launch {
            _songList.value = fetchAllAudioFiles()
        }
    }
}

