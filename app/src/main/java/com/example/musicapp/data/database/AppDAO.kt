package com.example.musicapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.musicapp.data.database.entity.PlayListEntity
import com.example.musicapp.data.database.entity.SongEntity

@Dao
interface AppDAO {

    // Song
    @Query("SELECT * FROM song")
    suspend fun getSongs(): List<SongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(songEntity: SongEntity): Long

    @Query("DELETE FROM song WHERE id = :id")
    suspend fun deleteSong(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSong(songEntity: SongEntity)


    // PlayList
    @Query("SELECT * FROM playlist")
    suspend fun getPlayLists(): List<PlayListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playListEntity: PlayListEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playListEntity: PlayListEntity)

    @Query("DELETE FROM playlist WHERE id = :id")
    suspend fun deletePlayList(id: Long)

    @Query("DELETE FROM song WHERE playlist_id = :playlistId")
    suspend fun deleteSongByPlaylistId(playlistId: Long)

    @Query("SELECT * FROM song WHERE playlist_id = :playlistId")
    suspend fun getSongsByPlayListId(playlistId: Long): List<SongEntity>

    @Query("SELECT * FROM playlist WHERE id = :id")
    fun getPlayList(id: Long): PlayListEntity?
}