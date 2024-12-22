package nd.phuc.cache.domain

import com.example.innertube.pages.PlaylistPage

interface CacheRepository {

    suspend fun insertPlaylist(playlistPage: PlaylistPage)

    suspend fun getPlaylist(id: String): PlaylistPage?

}