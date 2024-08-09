package com.example.mymusicapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "song", foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["id"],
        childColumns = ["play_list_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val artist: String,
    val path: String,
    @ColumnInfo(name = "play_list_id") val playListId: Long
)