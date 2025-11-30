package nd.phuc.musicapp.navigation

sealed class Screens() {
    object Home : Screens()

    object Playlists : Screens()

    object Library : Screens()

    data class PlaylistDetail(val playlistId: String) : Screens()
}
