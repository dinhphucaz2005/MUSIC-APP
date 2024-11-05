package com.example.musicapp

import android.content.Context
import android.util.Log
import com.example.musicapp.data.LocalDataSource
import com.example.musicapp.data.RoomDataSource
import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.getId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import javax.inject.Inject

@HiltAndroidTest
@RunWith(JUnit4::class)
class RoomDataSourceTest {
    companion object {
        private const val TAG = "RoomDataSourceTest"
    }

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dao: AppDAO

    @Inject
    lateinit var localDataSource: LocalDataSource

    @Inject
    lateinit var roomDataSource: RoomDataSource

    @Inject
    lateinit var context: Context

    private var localSongs = listOf<Song>()

    @Before
    fun setUp(): Unit = runBlocking {
        hiltRule.inject()
        localSongs = localDataSource.fetch(context)
        val testPlayList = "Test Playlist"
        roomDataSource.createNewPlayList(testPlayList).let {
            val newSongs = mutableListOf<Song>()
            for (i in 1..7) newSongs.add(localSongs.random())
            Log.d(TAG, "setUp: $testPlayList song: ${newSongs.map { it1 -> it1.id }}")
            roomDataSource.addSongs(newSongs, it.id)
        }
        val otherPlayList = "Other Playlist"
        roomDataSource.createNewPlayList(otherPlayList).let {
            val newSongs = mutableListOf<Song>()
            for (i in 1..10) newSongs.add(localSongs.random())
            Log.d(TAG, "setUp: $otherPlayList song: ${newSongs.map { it1 -> it1.id }}")
            roomDataSource.addSongs(newSongs, it.id)
        }
    }

    @Test
    fun test(): Unit = runBlocking {

        Log.d(TAG, "dao: ${dao.getPlayLists()}")
        Log.d(TAG, "dao: ${dao.getSongs()}")

        val savedPlayLists = roomDataSource.getSavedPlayLists(localSongs)
        savedPlayLists.forEach { playList ->
            Log.d(TAG, "PlayList: ${playList.id} ${playList.name}")
            Log.d(
                TAG, "Song: ${
                    playList.songs.map {
                        val id = it.getPath()?.let { it1 -> File(it1).getId() }
                        id
                    }
                }"
            )
        }
    }

}