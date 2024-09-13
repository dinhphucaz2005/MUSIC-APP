package com.example.musicapp

import com.example.musicapp.domain.model.Song
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test() {
        data class Song(val name: String, val author: String)

        val songs = listOf(
            Song("test1", "phuc"),
            Song("test2", "phuc"),
            Song("test3", "phuc"),
            Song("test4", "test"),
            Song("test5", "test"),
            Song("test6", "test")
        )

        songs.sortedWith(compareBy<Song> { it.author }.thenBy { it.name })

        println(songs)

    }
}