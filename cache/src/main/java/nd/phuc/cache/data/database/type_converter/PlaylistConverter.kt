package nd.phuc.cache.data.database.type_converter

import androidx.room.TypeConverter
import com.example.innertube.pages.PlaylistPage
import kotlinx.serialization.json.Json

internal class PlaylistConverter {

    @TypeConverter
    fun fromPlaylistPage(page: PlaylistPage): String {
        return Json.encodeToString(PlaylistPage.serializer(), page)
    }

    @TypeConverter
    fun toPlaylistPage(json: String): PlaylistPage {
        return Json.decodeFromString(json)
    }

}