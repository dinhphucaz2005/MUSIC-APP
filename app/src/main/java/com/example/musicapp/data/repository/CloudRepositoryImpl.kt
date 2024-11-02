package com.example.musicapp.data.repository

import android.util.Log
import com.example.musicapp.common.AppResource
import com.example.musicapp.domain.model.ServerSong
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.CloudRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val storage: StorageReference,
    private val database: DatabaseReference
) : CloudRepository {

    companion object {
        private const val TAG = "CloudRepositoryImpl"
    }

    private val page: Int = 0
    private val size: Int = 10

    /**
     * This method is deprecated and no longer used.
     * Use `loadMore()` instead.
     */
    @Deprecated(
        "No longer used. Use loadMore() instead.",
        ReplaceWith("loadMore()")
    )
    override suspend fun loadSongs(title: String, page: Int, size: Int): AppResource<List<Song>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadMore(): AppResource<List<ServerSong>> = try {
        val songList = mutableListOf<ServerSong>()
        Log.d(TAG, "loadMore: $songList")

        val startAfter = page * size
        val snapshot = database
            .child("songs")
            .orderByKey()
            .startAt(startAfter.toString())
            .limitToFirst(size)
            .get().await()

        for (child in snapshot.children) {
            val serverSong = child.getValue(ServerSong::class.java)
            serverSong?.let { songList.add(it) }
        }
        Log.d(TAG, "loadMore: $songList")
        AppResource.Success(songList)
    } catch (e: Exception) {
        AppResource.Error(e.message ?: "An error occurred")
    }

    override suspend fun upload(song: Song) {
        TODO("Not yet implemented")
    }
}