package com.example.musicapp.other.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicapp.other.data.database.entity.LikedSong

@Dao
interface LikedSongDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLikedSong(likedSong: LikedSong): Long

    @Query("SELECT * FROM liked_song")
    suspend fun getLikedSongs(): List<LikedSong>

    @Query("SELECT * FROM liked_song WHERE id = :id")
    suspend fun getLikedSongById(id: String): LikedSong?

    @Delete
    suspend fun deleteLikedSong(likedSong: LikedSong)

    @Query("DELETE FROM liked_song WHERE id = :id")
    suspend fun deleteLikedSongById(id: Long)

}