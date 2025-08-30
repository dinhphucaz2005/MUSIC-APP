package nd.phuc.musicapp.music.presentation.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import nd.phuc.core.model.LocalSong
import nd.phuc.core.model.Playlist
import nd.phuc.core.model.Song
import nd.phuc.core.model.SongId
import nd.phuc.musicapp.music.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            songRepository.getSongs()
        }
    }

    val songs: StateFlow<List<Song>> = songRepository.allSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val likedSongs: StateFlow<Set<SongId>> = songRepository.likedSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )


    val playlists: StateFlow<List<Playlist>> = songRepository.playlists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @Deprecated("No longer used")
    private val _localSongState = MutableStateFlow<LocalSongState>(LocalSongState.Idle)

    @Deprecated("No longer used")
    val localSongState: StateFlow<LocalSongState> = _localSongState.asStateFlow()


    @Deprecated("No longer used")
    fun loadSongs() {
        _localSongState.value = LocalSongState.Loading
        viewModelScope.launch {
            val songs = songRepository.getLocalSong()
            _localSongState.value = LocalSongState.Success(songs, System.currentTimeMillis())
        }
    }

    @Deprecated("No longer used")
    sealed class LocalSongState {

        object Idle : LocalSongState()

        object Loading : LocalSongState()

        data class Success(val songs: List<LocalSong>, val lastReloadMillis: Long) :
            LocalSongState()

        data class Error(val message: String) : LocalSongState()
    }

}

