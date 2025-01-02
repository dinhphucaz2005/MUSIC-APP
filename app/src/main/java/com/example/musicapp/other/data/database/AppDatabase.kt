package com.example.musicapp.other.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.data.database.entity.SearchConverters
import com.example.musicapp.other.data.database.entity.SearchEntity
import com.example.musicapp.other.data.database.entity.SongEntity
import com.example.musicapp.other.data.database.entity.SongEntityConverter

@Database(entities = [SongEntity::class, PlaylistEntity::class, SearchEntity::class], version = 1)
@TypeConverters(SongEntityConverter::class, SearchConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDAO(): AppDAO

    abstract fun cacheDao(): CacheDao

}