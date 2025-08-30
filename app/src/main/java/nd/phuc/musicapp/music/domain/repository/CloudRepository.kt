package nd.phuc.musicapp.music.domain.repository

import nd.phuc.core.model.FirebaseSong
import nd.phuc.core.model.LocalSong

interface CloudRepository {

    suspend fun load(): List<FirebaseSong>

    fun upload(localSongs: List<LocalSong>)
}