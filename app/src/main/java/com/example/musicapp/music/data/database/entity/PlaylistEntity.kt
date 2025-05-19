package com.example.musicapp.music.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "playlist")
@TypeConverters(SongConverter::class)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val createdBy: String = "",
    val coverUrl: String = "",
    val songs: List<SongEntity> = emptyList(),
)