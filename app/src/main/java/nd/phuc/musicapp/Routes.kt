package nd.phuc.musicapp

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Screens() : NavKey {
    @Serializable
    object Home : Screens()

    @Serializable
    object Playlists : Screens()

    @Serializable
    object Library : Screens()
}
