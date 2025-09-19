package nd.phuc.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nd.phuc.core.database.AppDatabase
import nd.phuc.core.database.SongDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).addCallback(object : RoomDatabase.Callback() {
//            override fun onOpen(db: SupportSQLiteDatabase) {
//                super.onOpen(db)
//
//                val cursor = db.query("SELECT COUNT(*) FROM playlist WHERE id = 0")
//                cursor.moveToFirst()
//                val count = cursor.getInt(0)
//                cursor.close()
//
//                if (count == 0) {
//                    db.execSQL("INSERT INTO playlist (id, name) VALUES (${PlaylistEntity.LIKED_PLAYLIST_ID}, 'Liked Songs')")
//                }
//            }
        }
        ).build()

    }

    @Provides
    @Singleton
    fun provideAppDao(database: AppDatabase): SongDao = database.songDao()

    @Provides
    @Singleton
    fun providePlaylistDao(database: AppDatabase) = database.playlistDao()

}
