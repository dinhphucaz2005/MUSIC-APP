package nd.phuc.core.model

sealed class SongId {

    data class Unidentified(val raw: String) : SongId()
    data class YouTube(val videoId: String) : SongId()
    data class Local(val uri: String) : SongId()
    data class Firebase(val id: String) : SongId()
}

sealed class PlaylistId {
    data class Saved(val id: Int) : PlaylistId()
}

data class UnidentifiedId(val raw: String)
