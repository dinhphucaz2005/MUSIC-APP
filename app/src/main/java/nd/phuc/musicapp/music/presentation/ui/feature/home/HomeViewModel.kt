package nd.phuc.musicapp.music.presentation.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nd.phuc.core.model.LocalSong
import nd.phuc.musicapp.music.domain.repository.LocalSongRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: LocalSongRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            songRepository.getSongs()
        }
    }

    val songs: StateFlow<List<LocalSong>> = songRepository.allSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val likedSongs: StateFlow<Set<LocalSong>> =
        songRepository.allSongs
            .map { songs -> songs.filter { it.isLiked }.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptySet()
            )
}

