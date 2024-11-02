package com.example.musicapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicapp.domain.model.PlayList


@Entity(tableName = "playlist")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
) {
    fun toModel() = PlayList(id, name)
}