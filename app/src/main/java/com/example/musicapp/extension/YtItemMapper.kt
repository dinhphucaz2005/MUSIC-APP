package com.example.musicapp.extension

import com.example.innertube.models.SongItem
import com.example.musicapp.other.domain.model.AudioSource
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.ThumbnailSource

fun SongItem.toSong(): Song {
    return Song(
        id = id,
        title = title,
        artist = artists.joinToString { it.name + ", " },
        audioSource = AudioSource.FromUrl(id),
        thumbnailSource = ThumbnailSource.FromUrl(thumbnail),
        durationMillis = (duration ?: 0) * 1000L,
    )
}