package com.example.musicapp

import com.example.musicapp.data.database.entity.PlayListEntity
import com.example.musicapp.data.database.entity.SongEntity
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.model.SongInfo
import com.example.musicapp.extension.getId
import com.example.musicapp.extension.toDuration
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.File
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

    @Test
    fun `mapping songs to SongInfo`() {

        val caching = mutableMapOf<Long, Int>() // <song id, index in local files>

        val songEntities = listOf(
            SongEntity(1, "Song 1", "/storage/emulated/0/Music/test1.mp3", 1),
            SongEntity(2, "Song 2", "/storage/emulated/0/Music/test2.mp3", 1),
            SongEntity(3, "Song 3", "/storage/emulated/0/Music/test3.mp3", 1),
            SongEntity(4, "Song 4", "/storage/emulated/0/Music/test4.mp3", 2),
            SongEntity(5, "Song 5", "/storage/emulated/0/Music/test5.mp3", 2)
        )

        songEntities.forEachIndexed { index, songEntity ->
            val fileId = File(songEntity.path).getId()
            caching[fileId] = index
        }

        val playListEntities = listOf(
            PlayListEntity(id = 1, name = "Playlist 1"), PlayListEntity(id = 2, name = "Playlist 2")
        )
        val playlist: List<PlayList> = playListEntities.map { playListEntity ->
            val songInfos = songEntities.filter { it.playlistId == playListEntity.id }.mapNotNull {
                val file = File(it.path)
//                if (file.exists())
                SongInfo(it.id, file.getId())
//                else
//                    null
            }
            PlayList(playListEntity.id, playListEntity.name, songInfos)
        }
        println(playlist)
    }
}