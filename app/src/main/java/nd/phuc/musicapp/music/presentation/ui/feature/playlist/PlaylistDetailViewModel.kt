package nd.phuc.musicapp.music.presentation.ui.feature.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import nd.phuc.musicapp.music.domain.model.Playlist
import nd.phuc.musicapp.music.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PlaylistDetailViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playlistId: String = checkNotNull(savedStateHandle["playlistId"])

    private val _playlistState = MutableStateFlow<PlaylistState>(PlaylistState.Idle)
    val playlistState: StateFlow<PlaylistState> = _playlistState.asStateFlow()

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        _playlistState.value = PlaylistState.Loading
        viewModelScope.launch {
            try {
                val playlist = playlistRepository.getPlaylist(playlistId)
                val songs = playlistRepository.getPlaylistSongs(playlistId)
                _playlistState.value = PlaylistState.Success(playlist, songs)
            } catch (e: Exception) {
                _playlistState.value = PlaylistState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class PlaylistState {
        object Idle : PlaylistState()
        object Loading : PlaylistState()
        data class Success(val playlist: Playlist, val songs: List<Song>) : PlaylistState()
        data class Error(val message: String) : PlaylistState()
    }
}