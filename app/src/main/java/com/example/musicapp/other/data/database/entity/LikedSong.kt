package com.example.musicapp.other.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_song")
data class LikedSong(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val title: String? = null,
    val artist: String? = null,
    val audioSource: String = "",
    val thumbnail: String? = null,
    val durationMillis: Long? = null,
    val type: Int = UNKNOWN
) {

    companion object {
        const val LOCAL = 0
        const val FIREBASE = 1
        const val YOUTUBE = 2
        const val UNKNOWN = -1
    }
}