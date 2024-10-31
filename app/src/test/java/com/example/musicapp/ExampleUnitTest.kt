package com.example.musicapp

import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.toDuration
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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

    @Test
    fun `test duration conversion for hours, minutes, and seconds`() {
        val input = 3661000L // 1 hour, 1 minute, 1 second
        val expectedOutput = "01:01:01"
        assertEquals(expectedOutput, input.toDuration())
    }

    @Test
    fun `test duration conversion for minutes and seconds`() {
        val input = 61000L // 1 minute, 1 second
        val expectedOutput = "01:01"
        assertEquals(expectedOutput, input.toDuration())
    }

    @Test
    fun `test duration conversion for just seconds`() {
        val input = 10000L // 10 seconds
        val expectedOutput = "00:10"
        assertEquals(expectedOutput, input.toDuration())
    }

    @Test
    fun `test duration conversion for zero milliseconds`() {
        val input = 0L // 0 milliseconds
        val expectedOutput = "00:00"
        assertEquals(expectedOutput, input.toDuration())
    }

    @Test
    fun `test duration conversion for multiple hours`() {
        val input = 7200000L // 2 hours
        val expectedOutput = "02:00:00"
        assertEquals(expectedOutput, input.toDuration())
    }
}