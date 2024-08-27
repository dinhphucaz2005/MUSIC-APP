package com.example.musicapp.extension

import com.example.musicapp.data.database.entity.SongEntity
import com.example.musicapp.domain.model.Song


fun Song.toSongEntity(playlistId: Long): SongEntity? {

    if (this.path == null)
        return null

    return SongEntity(
        title = this.fileName.getFileNameWithoutExtension(),
        path = this.path,
        playlistId = playlistId
    )
}