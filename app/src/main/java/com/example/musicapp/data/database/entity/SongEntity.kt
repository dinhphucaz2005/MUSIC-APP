package com.example.musicapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.musicapp.domain.model.AudioSource
import com.example.musicapp.domain.model.Song
import java.util.UUID

@Entity(
    tableName = "song", foreignKeys = [ForeignKey(
        entity = PlayListEntity::class,
        parentColumns = ["id"],
        childColumns = ["playlist_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SongEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val path: String,
    @ColumnInfo(name = "playlist_id") val playlistId: String
)

fun Song.toEntity(playlistId: String): SongEntity? {
    return when (audioSource) {
        is AudioSource.FromLocalFile -> SongEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            path = audioSource.uri.path ?: return null,
            playlistId = playlistId,
        )

        else -> null
    }
}