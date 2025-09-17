package nd.phuc.musicapp.music.data

import kotlinx.coroutines.flow.Flow
import nd.phuc.core.model.LocalSong
import nd.phuc.musicapp.music.data.database.AppDAO
import nd.phuc.musicapp.music.data.database.entity.SongEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSource @Inject constructor(
    private val dao: AppDAO,
) {
    fun getLikedSongs(): Flow<List<SongEntity>> = dao.getLikedSongs()
    suspend fun toggleLike(value: LocalSong) {
        val existing = dao.getSongByPath(value.filePath)
        if (existing != null) {
            dao.deleteSong(existing)
            return
        }
        dao.addSong(
            SongEntity(
                filePath = value.filePath,
                title = value.title,
            )
        )
    }

}
