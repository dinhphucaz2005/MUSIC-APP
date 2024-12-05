package com.example.musicapp.youtube.data.repository

import com.example.innertube.CustomYoutube
import com.example.innertube.YouTube
import com.example.innertube.models.SongItem
import com.example.innertube.models.WatchEndpoint
import com.example.musicapp.core.domain.DataError
import com.example.musicapp.core.domain.Result
import com.example.musicapp.youtube.domain.YoutubeRepository
import javax.inject.Inject

class DefaultYoutubeRepository @Inject constructor() : YoutubeRepository {

    override suspend fun searchSongs(query: String): Result<List<SongItem>, DataError.Remote> {
        val response = CustomYoutube.search(query, YouTube.SearchFilter.FILTER_VIDEO)

        return when {
            response.isSuccess -> Result.Success(
                response
                    .getOrNull()
                    ?.items
                    ?.filterIsInstance<SongItem>()
                    ?: emptyList()
            )

            else -> Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun relatedSongs(videoId: String): Result<List<SongItem>, DataError.Remote> {
        val watchEndpoint = WatchEndpoint(videoId)
        val response = CustomYoutube.next(watchEndpoint)
        return when {
            response.isSuccess -> Result.Success(
                response
                    .getOrNull()
                    ?.items
                    ?: emptyList()
            )

            else -> Result.Error(DataError.Remote.UNKNOWN)
        }
    }

}