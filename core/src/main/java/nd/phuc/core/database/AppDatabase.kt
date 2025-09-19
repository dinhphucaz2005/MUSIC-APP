package nd.phuc.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import nd.phuc.core.database.entity.PlaylistEntity
import nd.phuc.core.database.entity.SongEntity

@Database(entities = [SongEntity::class, PlaylistEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {

    internal abstract fun songDao(): SongDao

    internal abstract fun playlistDao(): PlaylistDao

}