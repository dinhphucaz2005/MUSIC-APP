package com.example.musicapp.youtube.domain

import com.example.innertube.models.SongItem
import com.example.musicapp.core.domain.DataError
import com.example.musicapp.core.domain.Result


interface YoutubeRepository {

    suspend fun searchSongs(query: String): Result<List<SongItem>, DataError.Remote>

    suspend fun relatedSongs(videoId: String): Result<List<SongItem>, DataError.Remote>
}