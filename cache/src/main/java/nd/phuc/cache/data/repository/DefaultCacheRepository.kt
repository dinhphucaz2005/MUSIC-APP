package nd.phuc.cache.data.repository

import com.example.innertube.pages.PlaylistPage
import nd.phuc.cache.data.database.PlaylistEntity
import nd.phuc.cache.data.database.PlaylistDao
import nd.phuc.cache.domain.CacheRepository

internal class DefaultCacheRepository(
    private val dao: PlaylistDao
) : CacheRepository {
    override suspend fun insertPlaylist(playlistPage: PlaylistPage) {
        dao.insert(PlaylistEntity(id = playlistPage.playlist.id, content = playlistPage))
    }

    override suspend fun getPlaylist(id: String): PlaylistPage? {
        return dao.getPlaylist(id)?.content
    }
}