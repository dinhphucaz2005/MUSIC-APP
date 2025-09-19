package nd.phuc.core.domain.model

data class Playlist<S : Song>(
    val id: Long,
    val name: String,
    val thumbnailSource: ThumbnailSource,
    val songs: List<S>,
) {
    val songCount: Int
        get() = songs.size
}