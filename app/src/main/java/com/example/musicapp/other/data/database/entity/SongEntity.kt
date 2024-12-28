package com.example.musicapp.other.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.innertube.models.Artist
import com.example.musicapp.other.domain.model.FirebaseSong
import com.example.musicapp.other.domain.model.LocalSong
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.YoutubeSong
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(
    tableName = "song"
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val otherId: String? = null,
    val title: String? = null,
    val artists: List<Artist>? = null,
    val audioSource: String,
    val thumbnail: String? = null,
    val type: Int,
    val durationMillis: Long? = null,
    @ColumnInfo(name = "playlist_id") val playlistId: Int
) {
    companion object {
        const val LOCAL_SONG = 0
        const val FIREBASE_SONG = 1
        const val YOUTUBE_SONG = 2

        fun create(song: Song, playlistId: Int): SongEntity? =
            when (song) {
                is LocalSong -> SongEntity(song, playlistId)
                is FirebaseSong -> SongEntity(song, playlistId)
                is YoutubeSong -> SongEntity(song, playlistId)
                else -> null
            }

    }


    constructor(song: LocalSong, playlistId: Int) : this(
        otherId = song.id,
        audioSource = song.uri.path.toString(),
        type = LOCAL_SONG,
        playlistId = playlistId
    )

    constructor(song: FirebaseSong, playlistId: Int) : this(
        otherId = song.id,
        title = song.title,
        audioSource = song.audioUrl,
        artists = listOf(Artist(song.artist, null)),
        thumbnail = song.thumbnailSource.getThumbnailUrl(),
        type = FIREBASE_SONG,
        durationMillis = song.durationMillis,
        playlistId = playlistId
    )

    constructor(song: YoutubeSong, playlistId: Int) : this(
        otherId = song.id,
        title = song.title,
        audioSource = song.id,
        artists = song.artists,
        thumbnail = song.thumbnail,
        durationMillis = song.durationMillis,
        type = YOUTUBE_SONG,
        playlistId = playlistId
    )

}

class SongEntityConverter {

    @TypeConverter
    fun fromArtists(artists: List<Artist>): String {
        return Json.encodeToString(artists)
    }

    @TypeConverter
    fun toArtists(artists: String): List<Artist> {
        return Json.decodeFromString(artists)
    }
}