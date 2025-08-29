package nd.phuc.musicapp.music.domain.model

import androidx.compose.runtime.Immutable


@Immutable
data class Playlist(
    val id: Int,
    val name: String,
    val songs: List<Song> = emptyList()
)