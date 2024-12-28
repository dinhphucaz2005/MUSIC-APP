package com.example.musicapp.other.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.musicapp.other.data.database.entity.PlaylistEntity
import com.example.musicapp.other.data.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDAO {

    // Song
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(songEntity: SongEntity): Long

    @Query("SELECT COUNT(*) FROM song WHERE audioSource = :audioSource AND playlist_id = :playlist")
    suspend fun isSongExists(audioSource: String, playlist: Int): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSong(songEntity: SongEntity)

    @Query("SELECT * FROM song WHERE playlist_id = :playlistId")
    suspend fun getSongsByPlaylistId(playlistId: Int): List<SongEntity>

    @Query("DELETE FROM song WHERE playlist_id = :playlistId")
    suspend fun deleteSongByPlaylistId(playlistId: Int)

    // PlayList
    @Query("SELECT * FROM playlist")
    fun getPlayLists(): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playListEntity: PlaylistEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playListEntity: PlaylistEntity)

    @Query("DELETE FROM playlist WHERE id = :id")
    suspend fun deletePlayList(id: Int)

    @Query("DELETE FROM song WHERE audioSource = :audioSource AND playlist_id = :playlistId")
    suspend fun deleteSongByAudioSource(audioSource: String, playlistId: Int)

    @Query("DELETE FROM song WHERE id = :id")
    suspend fun deleteSongById(id: Int)

    @Query("select * from song where playlist_id = :playlistId")
    fun getSongsFromPlaylist(playlistId: Int): Flow<List<SongEntity>>

}