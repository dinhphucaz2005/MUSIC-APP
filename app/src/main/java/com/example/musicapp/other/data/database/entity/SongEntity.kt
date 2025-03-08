package com.example.musicapp.other.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.player.model.Artist
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
    }
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