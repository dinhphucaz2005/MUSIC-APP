package nd.phuc.cache.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nd.phuc.cache.data.database.type_converter.PlaylistConverter

@Database(entities = [PlaylistEntity::class], version = 1)
@TypeConverters(PlaylistConverter::class)
abstract class YoutubeDatabase : RoomDatabase() {

    internal abstract fun playlistDao(): PlaylistDao

}