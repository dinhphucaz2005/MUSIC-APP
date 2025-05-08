package com.example.musicapp.music.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musicapp.music.data.database.entity.PlaylistEntity
import com.example.musicapp.music.data.database.entity.SongEntity
import com.example.musicapp.music.data.database.entity.SongEntityConverter

@Database(entities = [SongEntity::class, PlaylistEntity::class], version = 1)
@TypeConverters(SongEntityConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDAO(): AppDAO

}