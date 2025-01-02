package com.example.musicapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicapp.other.data.database.AppDAO
import com.example.musicapp.other.data.database.AppDatabase
import com.example.musicapp.other.data.database.CacheDao
import com.example.musicapp.other.data.repository.CacheRepositoryImpl
import com.example.musicapp.other.domain.repository.CacheRepository
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
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                db.query("SELECT COUNT(*) FROM searches").use {
                    if (it.moveToFirst()) {
                        val count = it.getInt(0)
                        if (count > 10) {
                            db.execSQL("DELETE FROM searches WHERE id IN (SELECT id FROM searches ORDER BY timestamp ASC LIMIT ${count - 10})")
                        } else if (count == 0) {
                            val currentTime = System.currentTimeMillis()
                            db.execSQL(
                                """
                                INSERT INTO searches (searchQuery, result, type, timestamp)
                                VALUES ('Dẫu chỉ là ký ức remix', '[]', 0, $currentTime),
                                        ('Sự thật sau một lời hứa chi dân', '[]', 0, $currentTime),
                                        ('Moonlight shadow', '[]', 0, $currentTime),
                                        ('Castle in the sky', '[]', 0, $currentTime),
                                        ('Weekend has come', '[]', 0, $currentTime),
                                        ('Masked Raver Vexento', '[]', 0, $currentTime),
                                        ('Khẩu thị tâm phi', '[]', 0, $currentTime),
                                        ('Somebody that i used to know', '[]', 0, $currentTime),
                                        ('Shadow of the sun', '[]', 0, $currentTime),
                                        ('Chờ anh nhé', '[]', 0, $currentTime);
                            """.trimIndent()
                            )
                        }
                    }
                }
            }
        }).build()

    }

    @Provides
    @Singleton
    fun provideAppDao(database: AppDatabase): AppDAO = database.appDAO()

    @Provides
    @Singleton
    fun provideCacheDao(database: AppDatabase): CacheDao = database.cacheDao()

    @Provides
    @Singleton
    fun provideCacheRepository(cacheDao: CacheDao): CacheRepository = CacheRepositoryImpl(cacheDao)
}
