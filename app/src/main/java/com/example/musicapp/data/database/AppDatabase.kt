package com.example.musicapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicapp.data.database.entity.PlayListEntity
import com.example.musicapp.data.database.entity.SongEntity

@Database(entities = [SongEntity::class, PlayListEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDAO(): AppDAO
}