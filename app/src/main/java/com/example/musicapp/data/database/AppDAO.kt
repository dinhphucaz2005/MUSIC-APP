package com.example.musicapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.musicapp.data.database.entity.PlaylistEntity
import com.example.musicapp.data.database.dto.PlaylistDTO
import com.example.musicapp.data.database.entity.SongEntity

@Dao
interface AppDAO {

    @Query("SELECT * FROM song")
    fun getSongs(): List<SongEntity>

    @Query("SELECT * FROM playlist")
    fun getAllPlayLists(): List<PlaylistEntity>

    @Query("SELECT * FROM song WHERE playlist_id = :playlist_id")
    fun getAllSongFromPlaylist(playlist_id: Int): SongEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSong(song: SongEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlayList(playListEntity: PlaylistEntity): Long

    @Query("DELETE FROM playlist WHERE id = :id")
    fun deletePlayList(id: Long)

    @Query("DELETE FROM song WHERE id = :id")
    fun deleteSongById(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSong(song: SongEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlayList(playListEntity: PlaylistEntity)

    @Query(
        """
            SELECT
                playlist.id AS `id`,
                playlist.name AS `name`,
                song.id as songId,
                song.title AS songTitle,
                song.path AS songPath
            FROM playlist
                LEFT JOIN song ON playlist.id = song.playlist_id
            ORDER BY playlist.id, song.id;
        """
    )
    fun getPlaylist(): List<PlaylistDTO>

    @Query("DELETE FROM song WHERE playlist_id = :playlistId")
    fun deleteSongByPlaylistId(playlistId: Long)
}