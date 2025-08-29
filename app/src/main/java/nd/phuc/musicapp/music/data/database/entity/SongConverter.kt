package nd.phuc.musicapp.music.data.database.entity

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SongConverter {
    @TypeConverter
    fun fromSongEntities(songs: List<SongEntity>): String {
        return Json.encodeToString(songs)
    }

    @TypeConverter
    fun toSongEntities(songs: String): List<SongEntity> {
        return Json.decodeFromString(songs)
    }
}