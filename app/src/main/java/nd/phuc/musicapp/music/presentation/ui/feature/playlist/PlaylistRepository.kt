package nd.phuc.musicapp.music.presentation.ui.feature.playlist

import nd.phuc.musicapp.di.FakeModule
import nd.phuc.core.model.Playlist
import nd.phuc.core.model.Song
import javax.inject.Inject
import kotlin.random.Random

class PlaylistRepository @Inject constructor() {
    fun getPlaylist(string: String) = Playlist(
        Random.nextInt(), string, songs = listOf(
            FakeModule.localSong,
            FakeModule.localSong,
            FakeModule.localSong,
            FakeModule.localSong,
            FakeModule.localSong,
        )
    )

    fun getPlaylistSongs(string: String) = listOf<Song>(
        FakeModule.localSong,
        FakeModule.localSong,
        FakeModule.localSong,
        FakeModule.localSong,
        FakeModule.localSong,
    )

    fun createPlaylist(name: String, description: String, thumbnailUrl: String?) {}

}
