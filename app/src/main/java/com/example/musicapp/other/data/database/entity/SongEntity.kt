package com.example.musicapp.other.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.musicapp.other.domain.model.LocalSong
import java.util.UUID

@Entity(
    tableName = "song",
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["id"],
        childColumns = ["playlist_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["playlist_id"])]
)
data class SongEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val path: String,
    @ColumnInfo(name = "playlist_id") val playlistId: String
)


fun LocalSong.toEntity(playlistId: String): SongEntity? =
    audio.path?.let {
        SongEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            path = it,
            playlistId = playlistId,
        )
    }