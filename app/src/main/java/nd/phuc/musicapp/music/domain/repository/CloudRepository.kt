package nd.phuc.musicapp.music.domain.repository

import nd.phuc.musicapp.music.domain.model.FirebaseSong
import nd.phuc.musicapp.music.domain.model.LocalSong

interface CloudRepository {

    suspend fun load(): List<FirebaseSong>

    fun upload(localSongs: List<LocalSong>)
}