package com.example.musicapp.other.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
)