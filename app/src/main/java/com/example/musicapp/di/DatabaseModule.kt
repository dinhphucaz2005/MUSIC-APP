package com.example.musicapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicapp.music.data.database.AppDAO
import com.example.musicapp.music.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
    fun provideAppDao(database: AppDatabase): AppDAO = database.appDAO()

}
