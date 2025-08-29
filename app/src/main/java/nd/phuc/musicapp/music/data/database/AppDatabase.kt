package nd.phuc.musicapp.music.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nd.phuc.musicapp.music.data.database.entity.PlaylistEntity
import nd.phuc.musicapp.music.data.database.entity.SongConverter
import nd.phuc.musicapp.music.data.database.entity.SongEntity

@Database(entities = [SongEntity::class, PlaylistEntity::class], version = 1)
@TypeConverters(SongConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDAO(): AppDAO

}