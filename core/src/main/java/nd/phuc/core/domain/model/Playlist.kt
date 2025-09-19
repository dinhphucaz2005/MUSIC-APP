package nd.phuc.core.domain.model

open class Playlist<S : Song>(
    val id: Long,
    val name: String,
    val thumbnailSource: ThumbnailSource,
    val songs: List<S>,
) {
    val songCount: Int
        get() = songs.size
}

class LikedSongsPlaylist<S : Song>(
    songs: List<S>,
) : Playlist<S>(
    id = LIKED_SONGS_PLAYLIST_ID,
    name = "Liked Songs",
    thumbnailSource = ThumbnailSource.None,
    songs = songs,
) {
    companion object {
        const val LIKED_SONGS_PLAYLIST_ID = -1L
    }
}