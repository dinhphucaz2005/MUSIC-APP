package com.example.innertube

import com.example.innertube.models.YouTubeClient
import com.example.innertube.models.response.BrowseResponse
import com.example.innertube.pages.BrowseResult
import com.example.innertube.pages.HomePage
import io.ktor.client.call.body


object CustomYoutube : YouTube() {

    suspend fun loadHome(): HomePage {
        val data = innerTube.browse(
            YouTubeClient.WEB_REMIX,
            "FEmusic_home",
            setLogin = true,
            params = null
        ).body<BrowseResponse>()
            .contents
            ?.singleColumnBrowseResultsRenderer?.tabs?.get(0)?.tabRenderer?.content
            ?.sectionListRenderer?.contents
        val result = data?.mapNotNull { row ->
            row.musicCarouselShelfRenderer?.let {
                HomePage.Section.fromMusicCarouselShelfRenderer(it)
            }
        }

        return HomePage(result ?: emptyList())
    }



}