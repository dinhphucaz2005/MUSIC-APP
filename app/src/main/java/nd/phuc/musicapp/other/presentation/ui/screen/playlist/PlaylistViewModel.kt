/*
package nd.phuc.musicapp.other.presentation.ui.screen.playlist

import androidx.lifecycle.ViewModel
import nd.phuc.core.extension.withIOContext
import nd.phuc.musicapp.music.data.database.entity.SongEntity
import nd.phuc.core.model.Playlist
import nd.phuc.musicapp.music.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
) : ViewModel() {

    data class PlaylistItem(
        val data: Playlist, val isSelected: Boolean = false
    )

    private val _isLoading = MutableStateFlow(false)

//    val playlists = songRepository
//        .getPlayLists()
//        .map { it.map { playlist -> PlaylistItem(playlist) } }
//        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val isLoading = _isLoading.asStateFlow()


    fun savePlaylist(name: String, description: String, createdBy: String, songs: List<SongEntity>) {
        withIOContext {
            songRepository.savePlaylist(name, description, createdBy, songs)
        }
    }
//    fun getPlaylists() = songRepository.getPlaylists()

//    fun createNewPlayList(name: String) {
//        withIOContext {
//            songRepository.createPlayList(name)
//        }
//    }
//
//    fun deleteLikedSong(song: Song) {
//        withIOContext {
//            songRepository.unlikeSong(song)
//        }
//    }

}*/
