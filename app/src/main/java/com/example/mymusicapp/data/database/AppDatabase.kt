package com.example.mymusicapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymusicapp.data.database.entity.PlaylistEntity
import com.example.mymusicapp.data.database.entity.SongEntity

@Database(entities = [SongEntity::class, PlaylistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDAO(): AppDAO
}