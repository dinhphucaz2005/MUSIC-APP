package com.example.musicapp.other.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicapp.other.data.database.entity.LikedSong
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.data.database.entity.SongEntity

@Database(entities = [SongEntity::class, PlaylistEntity::class, LikedSong::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDAO(): AppDAO

    abstract fun likedSongDAO(): LikedSongDAO
}