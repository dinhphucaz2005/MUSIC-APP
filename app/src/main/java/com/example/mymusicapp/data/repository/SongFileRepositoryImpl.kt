package com.example.mymusicapp.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.mymusicapp.data.dto.SongFileDTO
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.helper.BitmapHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SongFileRepositoryImpl(private val context: Context) : SongFileRepository {

    override suspend fun getAllAudioFiles(): ArrayList<Song> {
        return withContext(Dispatchers.IO) {
            val songs = arrayListOf<Song>()

            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
            )

            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

                while (cursor.moveToNext()) {
                    val title = cursor.getString(titleColumn)
                    val id = cursor.getLong(idColumn)
                    val data = cursor.getString(dataColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                    )

                    val bitmap = BitmapHelper.getMp3Thumbnail(data)
                    songs.add(
                        Song(title, contentUri.toString(), bitmap)
                    )
                }
            }
            songs
        }
    }
}