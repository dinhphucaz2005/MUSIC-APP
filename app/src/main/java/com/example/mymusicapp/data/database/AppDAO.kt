package com.example.mymusicapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mymusicapp.data.database.entity.PlaylistEntity
import com.example.mymusicapp.data.database.entity.SongEntity

@Dao
interface AppDAO {

    @Query("SELECT * FROM song")
    fun getSongs(): List<SongEntity>

    @Query("SELECT * FROM playlist")
    fun getPlayLists(): List<PlaylistEntity>

    @Insert
    fun addSong(song: SongEntity): Long

    @Insert
    fun addPlayList(playListEntity: PlaylistEntity): Long

    @Query("DELETE FROM playlist WHERE id = :id")
    fun deletePlayList(id: Int)

    @Query("DELETE FROM song WHERE id = :id")
    fun deleteSongById(id: Int)
}