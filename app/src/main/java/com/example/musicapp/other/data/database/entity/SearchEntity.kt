package com.example.musicapp.other.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.innertube.models.SongItem
import com.example.innertube.models.YTItem
import kotlinx.serialization.json.Json

enum class SearchType(val value: Int) {
    UNIDENTIFIED(-1),
    SONGS(0),
}

class SearchConverters {
    @TypeConverter
    fun fromSearchType(value: SearchType): Int {
        return value.value
    }

    @TypeConverter
    fun toSearchType(value: Int): SearchType {
        return when (value) {
            0 -> SearchType.SONGS
            else -> SearchType.UNIDENTIFIED
        }
    }
}

@Entity(tableName = "searches")
data class SearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val searchQuery: String?,
    val result: String?,
    val type: SearchType? = SearchType.UNIDENTIFIED,
    val timestamp: Long = System.currentTimeMillis()
) {

    fun toItem(): List<YTItem?> {
        if (result == null) return emptyList()
        return runCatching {
            when (type) {
                SearchType.SONGS -> Json.decodeFromString<List<SongItem?>>(result!!)
                else -> emptyList()
            }
        }.getOrNull() ?: emptyList()
    }

}