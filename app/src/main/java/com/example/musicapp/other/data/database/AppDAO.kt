package com.example.musicapp.other.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.data.database.entity.SongEntity

@Dao
interface AppDAO {

    // Song
    @Query("SELECT * FROM song")
    suspend fun getSongs(): List<SongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(songEntity: SongEntity): Long

    @Query("DELETE FROM song WHERE id = :id")
    suspend fun deleteSong(id: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSong(songEntity: SongEntity)


    // PlayList
    @Query("SELECT * FROM playlist")
    suspend fun getPlayLists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playListEntity: PlaylistEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playListEntity: PlaylistEntity)

    @Query("DELETE FROM playlist WHERE id = :id")
    suspend fun deletePlayList(id: String)

    @Query("DELETE FROM song WHERE playlist_id = :playlistId")
    suspend fun deleteSongByPlaylistId(playlistId: Long)

    @Query("SELECT * FROM song WHERE playlist_id = :playlistId")
    suspend fun getSongsByPlayListId(playlistId: String): List<SongEntity>

    @Query("SELECT * FROM playlist WHERE id = :id")
    fun getPlayList(id: String): PlaylistEntity?
}